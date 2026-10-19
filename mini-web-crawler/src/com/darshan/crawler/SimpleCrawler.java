package com.darshan.crawler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleCrawler {
    private static final int MAX_PAGES = 5;
    private final Set<String> visited = new HashSet<>();

    public void crawl(String targetUrl) {
        if (visited.size() >= MAX_PAGES || visited.contains(targetUrl)) {
            return;
        }
        visited.add(targetUrl);
        System.out.println("--------------------------------------------------");
        System.out.println("[CRAWLING] " + targetUrl);

        try {
            URL url = new URL(targetUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)");
            conn.setConnectTimeout(8000);
            conn.setReadTimeout(8000);

            int status = conn.getResponseCode();
            if (status != 200) {
                System.out.println("[STATUS] HTTP " + status);
                return;
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder html = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                html.append(line).append(" ");
            }
            in.close();

            String content = html.toString();

            // Extract Page Title
            Pattern titlePattern = Pattern.compile("<title[^>]*>(.*?)</title>", Pattern.CASE_INSENSITIVE);
            Matcher titleMatcher = titlePattern.matcher(content);
            if (titleMatcher.find()) {
                System.out.println("[TITLE]    " + titleMatcher.group(1).trim());
            }

            // Universal Link Regex (href="..." or href='...')
            Pattern linkPattern = Pattern.compile("href\\s*=\\s*[\"']([^\"'#\\s]+)[\"']", Pattern.CASE_INSENSITIVE);
            Matcher linkMatcher = linkPattern.matcher(content);

            Set<String> outbound = new HashSet<>();
            while (linkMatcher.find()) {
                String link = linkMatcher.group(1).trim();
                if (link.startsWith("http://") || link.startsWith("https://")) {
                    if (!visited.contains(link)) {
                        outbound.add(link);
                    }
                } else if (link.startsWith("/")) {
                    String base = url.getProtocol() + "://" + url.getHost();
                    String fullUrl = base + link;
                    if (!visited.contains(fullUrl)) {
                        outbound.add(fullUrl);
                    }
                }
            }

            System.out.println("[FOUND]    " + outbound.size() + " outbound link(s)");

            for (String nextUrl : outbound) {
                if (visited.size() < MAX_PAGES) {
                    crawl(nextUrl);
                }
            }

        } catch (Exception e) {
            System.err.println("[ERROR] Failed to crawl " + targetUrl + ": " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SimpleCrawler crawler = new SimpleCrawler();
        System.out.println("==================================================");
        System.out.println("           MINI WEB CRAWLER (CORE JAVA)           ");
        System.out.println("==================================================");

        String seedUrl = "";
        if (args.length > 0 && args[0] != null && !args[0].trim().isEmpty()) {
            seedUrl = args[0].trim();
        } else {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter URL to crawl: ");
            seedUrl = scanner.nextLine().trim();
        }

        if (!seedUrl.startsWith("http://") && !seedUrl.startsWith("https://")) {
            seedUrl = "https://" + seedUrl;
        }

        System.out.println("Target Seed URL: " + seedUrl);
        crawler.crawl(seedUrl);

        System.out.println("==================================================");
        System.out.println("           CRAWLING PROCESS COMPLETED             ");
        System.out.println("==================================================");
    }
}
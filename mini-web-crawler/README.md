# 🕷️ Mini Web Crawler & Indexer (Core Java)

[![Java](https://img.shields.io/badge/Java-17%2B-orange?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg?style=for-the-badge)](LICENSE)
[![Maintenance](https://img.shields.io/badge/Maintained%3F-yes-green.svg?style=for-the-badge)]()
[![Platform](https://img.shields.io/badge/Platform-Windows%20%7C%20Linux%20%7C%20macOS-lightgrey?style=for-the-badge)]()

A lightweight, high-performance recursive web crawler and metadata parser built from scratch using **Core Java**. It demonstrates fundamental web scraping mechanics, recursive graph traversal, connection throttling, and regular expression-based hyperlink extraction without heavy external dependencies.

---

## ✨ Key Features

- 🌐 **Recursive Web Graph Traversal:** Traverses web graphs up to configurable depth and page limits (`MAX_PAGES = 5`).
- 🎯 **Dynamic Target Input:** Supports seed URLs via interactive terminal prompt or direct command-line arguments (`args[0]`).
- ⚡ **Zero External Dependencies:** Built entirely with standard Java libraries (`java.net.HttpURLConnection`, `java.util.regex`).
- 📑 **Metadata & Title Parsing:** Extracts document titles (`<title>`) and normalizes HTML whitespace automatically.
- 🔗 **Universal Link Resolution:** Discovers both absolute (`https://...`) and relative (`/...`) URLs.
- 🛡️ **Deduplication & Politeness:** Uses `HashSet` lookups to prevent duplicate visits and avoid infinite crawl loops.

---

## 🏗️ Architecture & Workflow

```text
[ Seed URL ]
     │
     ▼
[ HTTP GET Request ] ───► [ Validate Response (HTTP 200) ]
                                    │
                                    ▼
                          [ Read HTML Stream ]
                                    │
          ┌─────────────────────────┴─────────────────────────┐
          ▼                                                   ▼
[ Extract <title> Tag ]                           [ Extract Outbound Links ]
          │                                                   │
          ▼                                                   ▼
   [ Print to CLI ]                                 [ Resolve Full URLs ]
                                                              │
                                                              ▼
                                                   [ Filter Unvisited URLs ]
                                                              │
                                                              ▼
                                                   [ Recursive Crawl Next ]

# ⚡ AI-Powered Online Exam Management System

A next-generation, dark-themed Java Swing desktop application that integrates local Large Language Models (LLMs) via **Ollama** to dynamically generate technical exams, evaluate students, and provide an interactive AI Tutor for post-exam review.

## 🚀 Key Features

* **Dynamic AI Exam Generation:** Connects to local Ollama (Qwen model) to generate highly specific, topic-based Multiple Choice Questions on the fly.
* **Strict Kiosk Execution Mode:** Built-in Anti-Cheat system that detects Window Focus Loss (Alt-Tab detection) and auto-submits the exam to prevent malpractices.
* **Smart UI/UX:** A premium, cyber-futuristic dark mode interface built entirely using custom Java Swing painting (`Graphics2D`, Anti-aliasing, and custom rounded panels).
* **AI Tutor & Mistake Analysis:** Post-exam split-screen dashboard that reviews incorrect answers with AI-generated explanations and includes a built-in AI chatbot for student doubts.
* **Real-time Leaderboard:** MySQL-backed global leaderboard and dynamic XP/Score tracking for students.

## 🛠️ Tech Stack

* **Frontend:** Java Swing (AWT, Custom UI Components)
* **Backend:** Java (JDBC, Multi-threading)
* **Database:** MySQL
* **AI Integration:** Ollama (Local LLM - `qwen3:8b` / `qwen2.5:1.5b`), `org.json` library for parsing.

## ⚙️ Setup & Installation

1. **Database Setup:**
   * Create a MySQL database and run the necessary queries to create the `users` table.
   * Ensure the `users` table has columns for `user_id`, `username`, `password`, and `score`.

2. **AI Setup (Ollama):**
   * Download and install [Ollama](https://ollama.com/).
   * Pull the required model by running: `ollama run qwen3:8b` (or your preferred model).
   * Ensure Ollama is running in the background on port `11434`.

3. **Project Setup:**
   * Clone this repository.
   * Add the `org.json.jar` and `mysql-connector-java.jar` to your project's build path.
   * Update the `DatabaseConnector.java` with your MySQL credentials.
   * Run `Main.java` to launch the application.

## 🔒 Security Features
This application overrides standard Window behaviors to ensure a secure exam environment. Exiting the fullscreen mode or switching to another application during an active session triggers an immediate security violation and auto-submits the current exam progress.

---
*Developed by [Steve](https://github.com/steve-dev-l7)*
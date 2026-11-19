package org.example;

import java.util.Scanner;

public class Main {
    private static final int MAX_SIZE = 1000;

    private static class TestResult {
        boolean isPalindrome;
        int cycles;
        long durationNs;

        TestResult(boolean isPalindrome, int cycles, long durationNs) {
            this.isPalindrome = isPalindrome;
            this.cycles = cycles;
            this.durationNs = durationNs;
        }
    }

    private static class Node {
        char data;
        Node next;
        Node(char data) { this.data = data; }
    }

    // --- A) STATIC STACK (Fixed Array) ---
    static class StaticStack {
        private final char[] arr = new char[MAX_SIZE];
        private int top = -1;

        public boolean isEmpty() { return top == -1; }
        public void push(char c) {
            if (top < MAX_SIZE - 1) arr[++top] = c;
        }
        public char pop() {
            return !isEmpty() ? arr[top--] : '\0';
        }
    }

    // --- A) DYNAMIC QUEUE (Linked List) ---
    static class DynamicQueue {
        private Node front = null, rear = null;

        public boolean isEmpty() { return front == null; }
        public void enqueue(char c) {
            Node newNode = new Node(c);
            if (rear == null) {
                front = rear = newNode;
                return;
            }
            rear.next = newNode;
            rear = newNode;
        }
        public char dequeue() {
            if (isEmpty()) return '\0';
            char c = front.data;
            front = front.next;
            if (front == null) rear = null;
            return c;
        }
    }

    // --- B) DYNAMIC STACK (Linked List) ---
    static class DynamicStack {
        private Node top = null;

        public boolean isEmpty() { return top == null; }
        public void push(char c) {
            Node newNode = new Node(c);
            newNode.next = top;
            top = newNode;
        }
        public char pop() {
            if (isEmpty()) return '\0';
            char c = top.data;
            top = top.next;
            return c;
        }
    }

    // --- B) STATIC QUEUE (Circular Array) ---
    static class StaticQueue {
        private final char[] arr = new char[MAX_SIZE];
        private int front = 0, rear = 0, count = 0;

        public boolean isEmpty() { return count == 0; }
        public void enqueue(char c) {
            if (count < MAX_SIZE) {
                arr[rear] = c;
                rear = (rear + 1) % MAX_SIZE;
                count++;
            }
        }
        public char dequeue() {
            if (isEmpty()) return '\0';
            char c = arr[front];
            front = (front + 1) % MAX_SIZE;
            count--;
            return c;
        }
    }

    public static TestResult checkMethodA(String word) {
        long start = System.nanoTime();
        int cycles = 0;
        boolean result = true;

        StaticStack s = new StaticStack();
        DynamicQueue q = new DynamicQueue();

        for (char c : word.toLowerCase().toCharArray()) {
            cycles++;
            s.push(c);
            q.enqueue(c);
        }

        while (!s.isEmpty() && !q.isEmpty()) {
            cycles++;
            if (s.pop() != q.dequeue()) {
                result = false;
                break;
            }
        }

        long duration = System.nanoTime() - start;
        return new TestResult(result, cycles, duration);
    }

    public static TestResult checkMethodB(String word) {
        long start = System.nanoTime();
        int cycles = 0;
        boolean result = true;

        DynamicStack s = new DynamicStack();
        StaticQueue q = new StaticQueue();

        for (char c : word.toLowerCase().toCharArray()) {
            cycles++;
            s.push(c);
            q.enqueue(c);
        }

        while (!s.isEmpty() && !q.isEmpty()) {
            cycles++;
            if (s.pop() != q.dequeue()) {
                result = false;
                break;
            }
        }

        long duration = System.nanoTime() - start;
        return new TestResult(result, cycles, duration);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a word: ");
        String word = scanner.next();

        TestResult resA = checkMethodA(word);
        TestResult resB = checkMethodB(word);

        System.out.println("\n--- Option A (Static Stack + Dynamic Queue) ---");
        System.out.println("Result: " + (resA.isPalindrome ? "Palindrome" : "Not a palindrome"));
        System.out.println("Cycles: " + resA.cycles);
        System.out.printf("Time:   %d ns (%.3f us)%n", resA.durationNs, resA.durationNs / 1000.0);

        System.out.println("\n--- Option B (Dynamic Stack + Static Queue) ---");
        System.out.println("Result: " + (resB.isPalindrome ? "Palindrome" : "Not a palindrome"));
        System.out.println("Cycles: " + resB.cycles);
        System.out.printf("Time:   %d ns (%.3f us)%n", resB.durationNs, resB.durationNs / 1000.0);

        scanner.close();
    }
}

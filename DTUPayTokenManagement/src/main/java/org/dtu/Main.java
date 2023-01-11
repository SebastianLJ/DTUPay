package org.dtu;

import messageQueue.DTUPayMessageQueue;
import messageQueue.QueueType;

public class Main {
    public static void main(String[] args) {
        DTUPayMessageQueue messageQueue = new DTUPayMessageQueue(QueueType.DTUPay_TokenManagement);
    }
}
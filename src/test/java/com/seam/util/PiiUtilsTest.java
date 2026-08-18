package com.seam.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PiiUtilsTest {

    @Test
    void containsPii_shouldReturnFalseForNullOrPlainText() {
        assertFalse(PiiUtils.containsPii(null));
        assertFalse(PiiUtils.containsPii("plain text without personal data"));
    }

    @Test
    void containsPii_shouldDetectResidentRegistrationNumber() {
        assertTrue(PiiUtils.containsPii("주민번호 900101-1234567"));
    }

    @Test
    void containsPii_shouldDetectBankAccountNumber() {
        assertTrue(PiiUtils.containsPii("계좌번호 1004-0000-1234"));
    }

    @Test
    void containsPii_shouldDetectPhoneNumber() {
        assertTrue(PiiUtils.containsPii("전화번호 010-1234-5678"));
    }

    @Test
    void maskPii_shouldReplaceAnyDetectedSensitiveValues() {
        String input = "주민번호 900101-1234567, 계좌 1004-0000-1234, 전화 010-1234-5678";

        String masked = PiiUtils.maskPii(input);

        assertNotNull(masked);
        assertFalse(masked.contains("900101-1234567"));
        assertFalse(masked.contains("1004-0000-1234"));
        assertFalse(masked.contains("010-1234-5678"));
        assertTrue(masked.contains("[REDACTED]"));
    }
}

package com.baeker.member.base.util;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.util.text.BasicTextEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JasyptConfigTest {

    @Value("${jasypt.encryptor.password}")
    private String key;

    @Test
    void 암호화() {
        String value = "";
        System.out.println(jasyptEncoding(value));
    }

    @Test
    void 복호화() {
        String value = "w10K+s4EnEwDbAxgBF+dbNEpajaAseXA";
        BasicTextEncryptor enc = new BasicTextEncryptor();
        enc.setPassword(key);
        System.out.println(enc.decrypt(value));
    }

    public String jasyptEncoding(String value) {
        StandardPBEStringEncryptor enc = new StandardPBEStringEncryptor();
        enc.setAlgorithm("PBEWithMD5AndDES");
        enc.setPassword(key);
        return enc.encrypt(value);
    }
}
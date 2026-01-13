package com.employee_sys.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * システム利用者のログイン情報を保持するBean
 */
@Data
@AllArgsConstructor
@NoArgsConstructor

public class User {
    private String username;
    private String password;

    @Override
    public String toString() {
        return "test";
    }

    // バリデーションの例
    public void validate() {
        assert username != null && !username.isEmpty() : "ユーザー名は空にできません";
        assert password != null && password.length() >= 6 : "パスワードは6文字以上必要です";
    }
}
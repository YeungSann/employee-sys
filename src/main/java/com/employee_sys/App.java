package com.employee_sys;

import com.employee_sys.ui.LoginUI;
import javax.swing.SwingUtilities;

/**
 * アプリケーションの起動クラスです。
 */
public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // LoginUIを初期化すると呼び出し
            new LoginUI();
        });
    }
}
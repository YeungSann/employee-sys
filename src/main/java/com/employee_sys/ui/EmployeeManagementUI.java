package com.employee_sys.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 従業員情報管理画面
 * 従業員の一覧表示、検索、追加、修正、削除機能を提供します。
 */
public class EmployeeManagementUI extends JFrame {

    private JTextField searchField;
    private JButton searchButton;
    private JButton addButton;
    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private JPopupMenu popupMenu;

    public EmployeeManagementUI() {
        this.initializeSettings();
        this.initComponents();
        this.setupEventListeners();
        this.setVisible(true);
    }

    /**
     * フレームの初期設定
     */
    protected void initializeSettings() {
        this.setTitle("従業員情報管理システム - メイン画面");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 列数増加に伴い、初期幅を広めに設定
        this.setSize(1000, 600);
        this.setLocationRelativeTo(null);
    }

    /**
     * コンポーネントの初期化とレイアウト構成
     */
    protected void initComponents() {
        this.setLayout(new BorderLayout());

        // 1. 上部パネル（検索および追加ボタン）
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        searchField = new JTextField(20);
        searchButton = new JButton("捜索");
        addButton = new JButton("新規追加");

        topPanel.add(new JLabel("検索ワード:"));
        topPanel.add(searchField);
        topPanel.add(searchButton);
        topPanel.add(addButton);

        // 2. 中央パネル（従業員一覧テーブル）
        this.setupTable();
        JScrollPane scrollPane = new JScrollPane(employeeTable);

        // 3. コンテキストメニュー（右クリックメニュー）のセットアップ
        this.setupPopupMenu();

        this.add(topPanel, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * テーブルモデルおよび表示形式の設定
     */
    private void setupTable() {
        // テーブルのヘッダー定義（計9列）
        String[] columnNames = {"ID", "名前", "性別", "年齢", "部署", "役職", "給与", "電話番号", "入社年月"};

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // セルの直接編集を禁止
                return false;
            }
        };

        this.loadSampleData();

        employeeTable = new JTable(tableModel);
        employeeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        employeeTable.setRowHeight(25);

        // 列幅の自動調整設定
        employeeTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    }

    /**
     * ポップアップメニューの設定
     */
    private void setupPopupMenu() {
        popupMenu = new JPopupMenu();
        JMenuItem editItem = new JMenuItem("修正");
        JMenuItem deleteItem = new JMenuItem("削除");
        popupMenu.add(editItem);
        popupMenu.add(deleteItem);

        // 修正アクション
        editItem.addActionListener(e -> {
            int selectedRow = employeeTable.getSelectedRow();
            if (selectedRow != -1) {
                new AddEmployeeUI(this, tableModel, selectedRow);
            }
        });

        // 削除アクション
        deleteItem.addActionListener(e -> {
            String id = getSelectedEmployeeId();
            if (id != null) {
                Object[] options = {"はい", "いいえ"}; // ボタンの言語を定義します
                int confirm = JOptionPane.showOptionDialog(
                        this,
                        "ID: " + id + " を削除しますか？",
                        "確認",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,     // 自ら定義している言語を使いません
                        options,  // 日本語で定義しているボタンを使います
                        options[0]
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    tableModel.removeRow(employeeTable.getSelectedRow());
                }
            }
        });
    }

    /**
     * 各コンポーネントのイベントリスナー設定
     */
    protected void setupEventListeners() {
        // 1. テーブル上での右クリックイベント
        employeeTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) { handlePopup(e); }
            @Override
            public void mouseReleased(MouseEvent e) { handlePopup(e); }
            private void handlePopup(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    int row = employeeTable.rowAtPoint(e.getPoint());
                    if (row >= 0) {
                        employeeTable.setRowSelectionInterval(row, row);
                        popupMenu.show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            }
        });

        // 2. 新規追加ボタン：インデックス -1 を渡して新規作成モードで起動
        addButton.addActionListener(e ->
                new AddEmployeeUI(this, tableModel, -1));

        // 3. 検索ボタン：あいまい検索を実行し、結果をダイアログで表示
        searchButton.addActionListener(e -> {
            String keyword = searchField.getText().trim();
            if (keyword.isEmpty()) return;

            // 検索結果格納用のテンポラリモデル
            DefaultTableModel resultModel = new DefaultTableModel(
                    new String[]{"ID", "名前", "性別", "年齢", "部署", "役職", "給与", "電話番号", "入社年月"}, 0);

            boolean found = false;
            // 全データを走査してキーワードに一致する行を特定（あいまい検索）
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                StringBuilder rowString = new StringBuilder();
                for (int j = 0; j < tableModel.getColumnCount(); j++) {
                    rowString.append(tableModel.getValueAt(i, j).toString());
                }

                // 行内のいずれかの項目にキーワードが含まれているかチェック
                if (rowString.toString().contains(keyword)) {
                    Object[] rowData = new Object[9];
                    for (int j = 0; j < 9; j++) rowData[j] = tableModel.getValueAt(i, j);
                    resultModel.addRow(rowData);
                    found = true;
                }
            }

            if (!found) {
                JOptionPane.showMessageDialog(this, "該当する従業員が見つかりません。");
            } else {
                // 検索結果を表示するダイアログの生成
                JDialog resultDialog = new JDialog(this, "検索結果", true);
                resultDialog.setSize(800, 400);
                resultDialog.add(new JScrollPane(new JTable(resultModel)));
                resultDialog.setLocationRelativeTo(this);
                resultDialog.setVisible(true);
            }
        });
    }

    /**
     * 選択されている行から従業員IDを取得
     */
    public String getSelectedEmployeeId() {
        int row = employeeTable.getSelectedRow();
        if (row != -1) {
            return (String) employeeTable.getValueAt(row, 0);
        }
        return null;
    }

    /**
     * テスト用のサンプルデータをロード
     */
    private void loadSampleData() {
        for (int i = 1; i <= 20; i++) {
            tableModel.addRow(new Object[]{
                    "EMP" + String.format("%03d", i),      // ID
                    "従業員 " + i,                         // 名前
                    (i % 2 == 0 ? "男" : "女"),            // 性別
                    20 + (i % 30),                         // 年齢
                    (i % 3 == 0 ? "営業部" : "技術部"),      // 部署
                    "正社員",                              // 役職
                    250000 + (i * 5000),                   // 給与
                    "080-1234-" + String.format("%04d", i), // 電話番号
                    "202" + (i % 5) + "-04-01"             // 入社年月
            });
        }
    }

    /**
     * アプリケーションの実行起点
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new EmployeeManagementUI();
        });
    }
}
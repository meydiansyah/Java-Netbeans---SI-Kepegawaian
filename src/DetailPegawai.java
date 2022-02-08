
import config.Koneksi;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author macbook
 */
public class DetailPegawai extends javax.swing.JFrame {

    /**
     * Creates new form DetailPegawai
     */
    
    Boolean editButton = false;
    Connection conn;
    Koneksi koneksi = new Koneksi();
    Statement st;
    ResultSet rs;
    String index;
    String indexAdmin;
    String indexUser;
    
    public DetailPegawai(String idUser, String idAdmin, String id) {
        indexUser = idUser;
        index = id;
        initComponents();
        button_setAdmin.setVisible(false);
        label_admin.setVisible(false);
        getIcon();
        changeView();
        setData();
    }

    private DetailPegawai() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private void setDropdownItem(ResultSet result) {
        dropdown_agama.removeAllItems();
        dropdown_jabatan.removeAllItems();
        
        dropdown_kelamin.removeAllItems();
        dropdown_kelamin.addItem("Laki - Laki");
        dropdown_kelamin.addItem("Perempuan");
        
        conn = koneksi.getConnection();
        try {
            st = conn.createStatement();
            rs = st.executeQuery("select * from agama");
            
            while(rs.next()) {
                dropdown_agama.addItem(rs.getString("nama_agama"));
            }
            
            ResultSet jb = st.executeQuery("select * from jabatan");
            
            while(jb.next()) 
                dropdown_jabatan.addItem(jb.getString("nama_jabatan"));
            
            dropdown_agama.setSelectedIndex(Integer.parseInt(result.getString("agama_id"))-1);
            dropdown_jabatan.setSelectedIndex(Integer.parseInt(result.getString("jabatan_id"))-1);

            if(result.getString("kelamin").equals("Perempuan")) {
                dropdown_kelamin.setSelectedIndex(1);
            } else {
                dropdown_kelamin.setSelectedIndex(0);
            }
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }        
        dropdown_agama.addActionListener(new ComboBoxListener());
        dropdown_kelamin.addActionListener(new ComboBoxListener());
    }
    
    private void setImageUser(ResultSet rs) throws SQLException {
        String url = rs.getString("image");
        ImageIcon icon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource(url)));
        Image img = icon.getImage();
        Image imgIcon = img.getScaledInstance(icon_img.getWidth(), icon_img.getHeight(), Image.SCALE_SMOOTH);
        
        ImageIcon i = new ImageIcon(imgIcon);
        img_user.setIcon(i);
    }
    
    private void setLabel(ResultSet r) throws SQLException {
        label_nama.setText(r.getString("nama"));
        label_tglLahir.setText(r.getString("tgl_lahir"));
        label_agama.setText(r.getString("nama_agama"));
        
        final String html = "<html><body style='width: %1spx'>%1s";
        
        label_alamat.setText(String.format(html, 150, r.getString("alamat")));
        label_bpjs.setText(r.getString("no_bpjs"));
        label_tenagaKerja.setText(r.getString("no_bpjs_ketenagakerjaan"));
        label_darah.setText(r.getString("gol_darah"));
        label_email.setText(r.getString("email"));
        label_jabatan.setText(r.getString("nama_jabatan"));
        label_kelamin.setText(r.getString("kelamin"));
        label_nik.setText(r.getString("nik"));
        label_nip.setText(r.getString("nip"));
        label_noKK.setText(r.getString("no_kk"));
        label_pendidikan.setText(r.getString("pendidikan"));
        label_telepon.setText(r.getString("telepon"));
        label_npwp.setText(r.getString("npwp"));
    }
    
    private void setTextField(ResultSet r) throws SQLException {
        textfield_nama.setText(r.getString("nama"));
        textfield_tglLahir.setText(r.getString("tgl_lahir"));
        
        textfield_alamat.setText(r.getString("alamat"));
        textfield_bpjs.setText(r.getString("no_bpjs"));
        textfield_tenagaKerja.setText(r.getString("no_bpjs_ketenagakerjaan"));
        textfield_darah.setText(r.getString("gol_darah"));
        textfield_email.setText(r.getString("email"));
        textfield_nik.setText(r.getString("nik"));
        textfield_nip.setText(r.getString("nip"));
        textfield_noKK.setText(r.getString("no_kk"));
        textfield_pendidikan.setText(r.getString("pendidikan"));
        textfield_telepon.setText(r.getString("telepon"));
        textfield_npwp.setText(r.getString("npwp"));
    }
    
    private void checkAdmin(ResultSet rs) throws SQLException {
        String id = rs.getString("id");
        conn = koneksi.getConnection();
        try {
            st = conn.createStatement();
            rs = st.executeQuery("select pegawai_id from admin where pegawai_id = " + id );
            
            if(rs.next()) {
                button_setAdmin.setVisible(false);
                button_hapus.setVisible(false);
                label_admin.setVisible(true);
            } else {
                button_hapus.setVisible(true);

            }
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    private void checkJabatan(ResultSet rs) throws SQLException {
        if(rs.getString("jabatan_id").equals("1")) 
                button_setAdmin.setVisible(true);
    }
    
    private void setData() {
        conn = koneksi.getConnection();
        try {
            st = conn.createStatement();
            rs = st.executeQuery("SELECT * FROM pegawai INNER JOIN user ON pegawai.user_id = user.id INNER JOIN jabatan ON pegawai.jabatan_id = jabatan.id INNER JOIN agama on user.agama_id = agama.id where pegawai.id = " + index);
            rs.next();
            
            setLabel(rs);
            setTextField(rs);
            checkJabatan(rs);
            checkAdmin(rs);
            setImageUser(rs);
            setDropdownItem(rs);
            
        } catch (SQLException e) {
            System.out.println("Set Data : " + e.getMessage());
        }
    }
    
    private void changeView() {
        if(editButton) {
            button_edit.setText("Simpan");
            switchLabel(false);
            button_cancel.setVisible(true);
            switchTextfield(true);
        }
        else{
            button_edit.setText("Edit");
            switchLabel(true);
            button_cancel.setVisible(false);
            switchTextfield(false);
        }
    }
    
    private void switchLabel(Boolean bool) { 
        label_nama.setVisible(bool);
        label_tglLahir.setVisible(bool);
        label_agama.setVisible(bool);
        label_alamat.setVisible(bool);
        label_bpjs.setVisible(bool);
        label_tenagaKerja.setVisible(bool);
        label_darah.setVisible(bool);
        label_email.setVisible(bool);
        label_jabatan.setVisible(bool);
        label_kelamin.setVisible(bool);
        label_nik.setVisible(bool);
        label_nip.setVisible(bool);
        label_noKK.setVisible(bool);
        label_pendidikan.setVisible(bool);
        label_telepon.setVisible(bool);
        label_npwp.setVisible(bool);
    }
    
    private void switchTextfield(Boolean bool) {
        textfield_nama.setVisible(bool);
        textfield_tglLahir.setVisible(bool);
        
        dropdown_agama.setVisible(bool);
        dropdown_jabatan.setVisible(bool);
        dropdown_kelamin.setVisible(bool);
        
        textfield_alamat.setVisible(bool);
        textfield_bpjs.setVisible(bool);
        textfield_tenagaKerja.setVisible(bool);
        textfield_darah.setVisible(bool);
        textfield_email.setVisible(bool);
        textfield_nik.setVisible(bool);
        textfield_nip.setVisible(bool);
        textfield_noKK.setVisible(bool);
        textfield_pendidikan.setVisible(bool);
        textfield_telepon.setVisible(bool);
        textfield_npwp.setVisible(bool);
    }
    
    private void getIcon() {
        ImageIcon icon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/img/admin.png")));
        Image img = icon.getImage();
        Image imgIcon = img.getScaledInstance(icon_img.getWidth(), icon_img.getHeight(), Image.SCALE_SMOOTH);
        
        ImageIcon i = new ImageIcon(imgIcon);
        icon_img.setIcon(i);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        icon_img = new javax.swing.JLabel();
        textfield_nik = new javax.swing.JTextField();
        label_nik = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        textfield_nip = new javax.swing.JTextField();
        label_nip = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        label_nama = new javax.swing.JLabel();
        textfield_nama = new javax.swing.JTextField();
        label_kelamin = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        textfield_email = new javax.swing.JTextField();
        label_email = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        label_tglLahir = new javax.swing.JLabel();
        textfield_tglLahir = new javax.swing.JTextField();
        textfield_telepon = new javax.swing.JTextField();
        label_telepon = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        button_edit = new javax.swing.JButton();
        textfield_npwp = new javax.swing.JTextField();
        label_npwp = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        label_noKK = new javax.swing.JLabel();
        textfield_noKK = new javax.swing.JTextField();
        label_jabatan = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        textfield_bpjs = new javax.swing.JTextField();
        label_bpjs = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        textfield_darah = new javax.swing.JTextField();
        textfield_tenagaKerja = new javax.swing.JTextField();
        jLabel42 = new javax.swing.JLabel();
        label_agama = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        label_tenagaKerja = new javax.swing.JLabel();
        label_darah = new javax.swing.JLabel();
        textfield_pendidikan = new javax.swing.JTextField();
        jLabel46 = new javax.swing.JLabel();
        label_pendidikan = new javax.swing.JLabel();
        button_cancel = new javax.swing.JButton();
        button_hapus = new javax.swing.JButton();
        img_user = new javax.swing.JLabel();
        dropdown_agama = new javax.swing.JComboBox<>();
        jPanel1 = new javax.swing.JPanel();
        jLabel39 = new javax.swing.JLabel();
        label_alamat = new javax.swing.JLabel();
        textfield_alamat = new javax.swing.JTextField();
        dropdown_jabatan = new javax.swing.JComboBox<>();
        button_setAdmin = new javax.swing.JButton();
        dropdown_kelamin = new javax.swing.JComboBox<>();
        label_admin = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.LINE_AXIS));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("DIN Alternate", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(25, 62, 138));
        jLabel1.setText("Data Pegawai");

        textfield_nik.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                textfield_nikKeyTyped(evt);
            }
        });

        label_nik.setText("Nama User");

        jLabel22.setFont(new java.awt.Font("DIN Alternate", 1, 18)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(25, 62, 138));
        jLabel22.setText("NIK");

        textfield_nip.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                textfield_nipKeyTyped(evt);
            }
        });

        label_nip.setText("Nama User");

        jLabel24.setFont(new java.awt.Font("DIN Alternate", 1, 18)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(25, 62, 138));
        jLabel24.setText("Nama");

        jLabel25.setFont(new java.awt.Font("DIN Alternate", 1, 18)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(25, 62, 138));
        jLabel25.setText("NIP");

        label_nama.setText("Nama User");

        label_kelamin.setText("Nama User");

        jLabel30.setFont(new java.awt.Font("DIN Alternate", 1, 18)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(25, 62, 138));
        jLabel30.setText("Kelamin");

        label_email.setText("Nama User");

        jLabel32.setFont(new java.awt.Font("DIN Alternate", 1, 18)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(25, 62, 138));
        jLabel32.setText("Tanggal Lahir");

        jLabel33.setFont(new java.awt.Font("DIN Alternate", 1, 18)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(25, 62, 138));
        jLabel33.setText("Email");

        label_tglLahir.setText("Nama User");

        textfield_telepon.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                textfield_teleponKeyTyped(evt);
            }
        });

        label_telepon.setText("Nama User");

        jLabel36.setFont(new java.awt.Font("DIN Alternate", 1, 18)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(25, 62, 138));
        jLabel36.setText("Telepon");

        button_edit.setText("Edit");
        button_edit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                button_editMouseClicked(evt);
            }
        });
        button_edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_editActionPerformed(evt);
            }
        });

        textfield_npwp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                textfield_npwpKeyTyped(evt);
            }
        });

        label_npwp.setText("Nama User");

        jLabel35.setFont(new java.awt.Font("DIN Alternate", 1, 18)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(25, 62, 138));
        jLabel35.setText("No. KK");

        jLabel37.setFont(new java.awt.Font("DIN Alternate", 1, 18)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(25, 62, 138));
        jLabel37.setText("NPWP");

        label_noKK.setText("Nama User");

        textfield_noKK.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                textfield_noKKKeyTyped(evt);
            }
        });

        label_jabatan.setText("Nama User");

        jLabel38.setFont(new java.awt.Font("DIN Alternate", 1, 18)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(25, 62, 138));
        jLabel38.setText("Jabatan");

        textfield_bpjs.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                textfield_bpjsKeyTyped(evt);
            }
        });

        label_bpjs.setText("Nama User");

        jLabel40.setFont(new java.awt.Font("DIN Alternate", 1, 18)); // NOI18N
        jLabel40.setForeground(new java.awt.Color(25, 62, 138));
        jLabel40.setText("BPJS");

        jLabel41.setFont(new java.awt.Font("DIN Alternate", 1, 18)); // NOI18N
        jLabel41.setForeground(new java.awt.Color(25, 62, 138));
        jLabel41.setText("BPJS Ketenagakerjaan");

        textfield_tenagaKerja.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                textfield_tenagaKerjaKeyTyped(evt);
            }
        });

        jLabel42.setFont(new java.awt.Font("DIN Alternate", 1, 18)); // NOI18N
        jLabel42.setForeground(new java.awt.Color(25, 62, 138));
        jLabel42.setText("Pendidikan");

        label_agama.setText("Nama User");

        jLabel44.setFont(new java.awt.Font("DIN Alternate", 1, 18)); // NOI18N
        jLabel44.setForeground(new java.awt.Color(25, 62, 138));
        jLabel44.setText("Golongan Darah");

        label_tenagaKerja.setText("Nama User");

        label_darah.setText("Nama User");

        jLabel46.setFont(new java.awt.Font("DIN Alternate", 1, 18)); // NOI18N
        jLabel46.setForeground(new java.awt.Color(25, 62, 138));
        jLabel46.setText("Agama");

        label_pendidikan.setText("Nama User");

        button_cancel.setText("Cancel");
        button_cancel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                button_cancelMouseClicked(evt);
            }
        });

        button_hapus.setText("Hapus");
        button_hapus.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                button_hapusMouseClicked(evt);
            }
        });

        dropdown_agama.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel39.setFont(new java.awt.Font("DIN Alternate", 1, 18)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(25, 62, 138));
        jLabel39.setText("Alamat");
        jPanel1.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        label_alamat.setText("Nama User");
        jPanel1.add(label_alamat, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, 213, 45));
        jPanel1.add(textfield_alamat, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 213, 30));

        dropdown_jabatan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        button_setAdmin.setText("Jadikan admin");
        button_setAdmin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                button_setAdminMouseClicked(evt);
            }
        });

        dropdown_kelamin.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        label_admin.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        label_admin.setText("Sebagai Admin");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(76, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel22)
                            .addComponent(label_nik)
                            .addComponent(label_nip)
                            .addComponent(jLabel24)
                            .addComponent(label_nama)
                            .addComponent(jLabel37)
                            .addComponent(jLabel40)
                            .addComponent(label_bpjs)
                            .addComponent(label_npwp)
                            .addComponent(jLabel35)
                            .addComponent(jLabel38)
                            .addComponent(label_jabatan)
                            .addComponent(label_noKK)
                            .addComponent(jLabel25)
                            .addComponent(textfield_npwp)
                            .addComponent(textfield_noKK)
                            .addComponent(textfield_nik)
                            .addComponent(textfield_nip)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(textfield_nama)
                            .addComponent(textfield_bpjs)
                            .addComponent(dropdown_jabatan, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel33)
                            .addComponent(jLabel30)
                            .addComponent(label_kelamin)
                            .addComponent(label_email)
                            .addComponent(jLabel32)
                            .addComponent(jLabel36)
                            .addComponent(label_telepon)
                            .addComponent(textfield_telepon, javax.swing.GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE)
                            .addComponent(label_tglLahir)
                            .addComponent(jLabel46)
                            .addComponent(jLabel41)
                            .addComponent(label_tenagaKerja)
                            .addComponent(textfield_tenagaKerja, javax.swing.GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE)
                            .addComponent(label_agama)
                            .addComponent(jLabel44)
                            .addComponent(jLabel42)
                            .addComponent(label_pendidikan)
                            .addComponent(textfield_pendidikan, javax.swing.GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE)
                            .addComponent(label_darah)
                            .addComponent(textfield_darah, javax.swing.GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE)
                            .addComponent(dropdown_agama, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(textfield_email)
                            .addComponent(textfield_tglLahir)
                            .addComponent(dropdown_kelamin, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(img_user, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 97, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(icon_img, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(button_hapus)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(button_cancel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(button_edit))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(label_admin)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(button_setAdmin)))))
                .addContainerGap(77, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(icon_img, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(button_edit)
                            .addComponent(button_cancel)
                            .addComponent(button_hapus))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(button_setAdmin)
                            .addComponent(label_admin)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(29, 29, 29)
                        .addComponent(img_user, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, 46, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel32)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(label_tglLahir, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(textfield_tglLahir, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(jLabel36)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label_telepon, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(textfield_telepon, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel33)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(label_email, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(textfield_email, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel30)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label_kelamin, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(dropdown_kelamin, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel44)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(label_darah, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(textfield_darah, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel42)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label_pendidikan, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(textfield_pendidikan, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel46)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label_agama, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(dropdown_agama, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel41)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label_tenagaKerja, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(textfield_tenagaKerja, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel24)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(label_nama, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(textfield_nama, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel25)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label_nip, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(textfield_nip, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel22)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label_nik, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(textfield_nik, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel35)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(label_noKK, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(textfield_noKK, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel38)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label_jabatan, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dropdown_jabatan, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(16, 16, 16)
                        .addComponent(jLabel37)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label_npwp, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(textfield_npwp, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel40)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label_bpjs, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(textfield_bpjs, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(61, Short.MAX_VALUE))
        );

        jScrollPane1.setViewportView(jPanel2);

        getContentPane().add(jScrollPane1);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void editUser(String sql) {
        conn = koneksi.getConnection();
        try {

            st = conn.createStatement();
            st.execute(sql);

        } catch(SQLException e) {
            System.out.println("edit : " + e.getMessage());
        }
    }
    
    private void button_editMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_editMouseClicked
        // TODO add your handling code here:
        if(button_edit.getText().equals("Simpan")) {
            int opt = JOptionPane.showConfirmDialog(null, "Simpan Perubahan ?");
            
            if(opt == JOptionPane.YES_OPTION) {
           
                String img;
                if((dropdown_kelamin.getSelectedItem()).equals("Perempuan")) {
                    img = "/img/pegawai_woman.png";
                } else {
                    img = "/img/pegawai_man.png";
                }
                
                String sql = "update pegawai inner join user on pegawai.user_id = user.id set " 
                        + "user.nama = '" + textfield_nama.getText() + "', "
                        + "user.tgl_lahir = '" + textfield_tglLahir.getText() + "', "
                        + "user.nik = '" + textfield_nik.getText() + "', "
                        + "user.alamat = '" + textfield_alamat.getText() + "', "
                        + "user.telepon = '" + textfield_telepon.getText() + "', "
                        + "user.email = '" + textfield_email.getText() + "', "
                        + "user.gol_darah = '" + textfield_darah.getText() + "', "
                        + "user.kelamin = '" + dropdown_kelamin.getSelectedItem() + "', "
                        + "user.pendidikan = '" + textfield_pendidikan.getText() + "', "
                        + "user.image = '" + img + "', "
                        + "user.agama_id = " + (dropdown_agama.getSelectedIndex()+1) + ", "
                        + "pegawai.nip = '" + textfield_nip.getText() + "', "
                        + "pegawai.jabatan_id = " + (dropdown_jabatan.getSelectedIndex()+1) + ", "
                        + "pegawai.npwp = '" + textfield_npwp.getText() + "', "
                        + "pegawai.no_bpjs = '" + textfield_bpjs.getText() + "', "
                        + "pegawai.no_bpjs_ketenagakerjaan = '" + textfield_tenagaKerja.getText() + "' "
                        + "where pegawai.id = " + index;
                
                editUser(sql);
                
                setVisible(false);
                HomePage hp = new HomePage(indexAdmin);
                hp.dispose();
                hp.setVisible(true);
                
            }
        } else {
            editButton = true;
            changeView();
        }
        
    }//GEN-LAST:event_button_editMouseClicked

    private void button_cancelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_cancelMouseClicked
        // TODO add your handling code here:
        editButton = false;
        changeView();
    }//GEN-LAST:event_button_cancelMouseClicked

    private void button_hapusMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_hapusMouseClicked
        // TODO add your handling code here:
        int opt = JOptionPane.showConfirmDialog(null, "Hapus Data ?");
            
        if(opt == JOptionPane.YES_OPTION) {
            conn = koneksi.getConnection();
            try {
                st = conn.createStatement();
                st.execute("delete from user where id = " + indexUser);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            JOptionPane.showMessageDialog(null, "Data berhasil dihapus !");
            dispose();
            HomePage hp = new HomePage(indexAdmin);
            hp.dispose();
            hp.setVisible(true);
        }
    }//GEN-LAST:event_button_hapusMouseClicked

    private void button_editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_editActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_button_editActionPerformed

    private void textfield_teleponKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textfield_teleponKeyTyped
        // TODO add your handling code here:
        char enter = evt.getKeyChar();
        if(!(Character.isDigit(enter))) {
            evt.consume();
        }
    }//GEN-LAST:event_textfield_teleponKeyTyped

    private void textfield_nipKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textfield_nipKeyTyped
        // TODO add your handling code here:
        char enter = evt.getKeyChar();
        if(!(Character.isDigit(enter))) {
            evt.consume();
        }
    }//GEN-LAST:event_textfield_nipKeyTyped

    private void textfield_nikKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textfield_nikKeyTyped
        // TODO add your handling code here:
        char enter = evt.getKeyChar();
        if(!(Character.isDigit(enter))) {
            evt.consume();
        }
    }//GEN-LAST:event_textfield_nikKeyTyped

    private void textfield_noKKKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textfield_noKKKeyTyped
        // TODO add your handling code here:
        char enter = evt.getKeyChar();
        if(!(Character.isDigit(enter))) {
            evt.consume();
        }
    }//GEN-LAST:event_textfield_noKKKeyTyped

    private void textfield_npwpKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textfield_npwpKeyTyped
        // TODO add your handling code here:
        char enter = evt.getKeyChar();
        if(!(Character.isDigit(enter))) {
            evt.consume();
        }
    }//GEN-LAST:event_textfield_npwpKeyTyped

    private void textfield_bpjsKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textfield_bpjsKeyTyped
        // TODO add your handling code here:
        char enter = evt.getKeyChar();
        if(!(Character.isDigit(enter))) {
            evt.consume();
        }
    }//GEN-LAST:event_textfield_bpjsKeyTyped

    private void textfield_tenagaKerjaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textfield_tenagaKerjaKeyTyped
        // TODO add your handling code here:char enter = evt.getKeyChar();
        char enter = evt.getKeyChar();
        if(!(Character.isDigit(enter))) {
            evt.consume();
        }
    }//GEN-LAST:event_textfield_tenagaKerjaKeyTyped

    private void button_setAdminMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_setAdminMouseClicked
        // TODO add your handling code here:
        int opt = JOptionPane.showConfirmDialog(null, "Tambahkan sebagai admin ?");
        
        if(opt == JOptionPane.YES_OPTION) {
            setVisible(false);
            TambahAdmin tmbh = new TambahAdmin(indexAdmin, index);
            tmbh.setVisible(true);
        }
    }//GEN-LAST:event_button_setAdminMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DetailPegawai.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DetailPegawai.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DetailPegawai.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DetailPegawai.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DetailPegawai().setVisible(true);
            }
        });
    }
    
    private class ComboBoxListener implements ActionListener{
 
        @Override
        public void actionPerformed(ActionEvent e) {
            String status = (String) dropdown_agama.getSelectedItem();
        }
        
    }
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton button_cancel;
    private javax.swing.JButton button_edit;
    private javax.swing.JButton button_hapus;
    private javax.swing.JButton button_setAdmin;
    private javax.swing.JComboBox<String> dropdown_agama;
    private javax.swing.JComboBox<String> dropdown_jabatan;
    private javax.swing.JComboBox<String> dropdown_kelamin;
    private javax.swing.JLabel icon_img;
    private javax.swing.JLabel img_user;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel label_admin;
    private javax.swing.JLabel label_agama;
    private javax.swing.JLabel label_alamat;
    private javax.swing.JLabel label_bpjs;
    private javax.swing.JLabel label_darah;
    private javax.swing.JLabel label_email;
    private javax.swing.JLabel label_jabatan;
    private javax.swing.JLabel label_kelamin;
    private javax.swing.JLabel label_nama;
    private javax.swing.JLabel label_nik;
    private javax.swing.JLabel label_nip;
    private javax.swing.JLabel label_noKK;
    private javax.swing.JLabel label_npwp;
    private javax.swing.JLabel label_pendidikan;
    private javax.swing.JLabel label_telepon;
    private javax.swing.JLabel label_tenagaKerja;
    private javax.swing.JLabel label_tglLahir;
    private javax.swing.JTextField textfield_alamat;
    private javax.swing.JTextField textfield_bpjs;
    private javax.swing.JTextField textfield_darah;
    private javax.swing.JTextField textfield_email;
    private javax.swing.JTextField textfield_nama;
    private javax.swing.JTextField textfield_nik;
    private javax.swing.JTextField textfield_nip;
    private javax.swing.JTextField textfield_noKK;
    private javax.swing.JTextField textfield_npwp;
    private javax.swing.JTextField textfield_pendidikan;
    private javax.swing.JTextField textfield_telepon;
    private javax.swing.JTextField textfield_tenagaKerja;
    private javax.swing.JTextField textfield_tglLahir;
    // End of variables declaration//GEN-END:variables
}

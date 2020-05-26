package nguyentrinhan70.example.com.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class TimKiemCDDVDUI extends JDialog {
	
	JTextField txtLoaiCD, txtNamXuatBan;
	JButton btnTim;
	DefaultTableModel dtmCd;
	JTable tblCd;
	
	Connection conn= null;
	PreparedStatement preparedStatement = null;
	ResultSet resultSet = null;
	
	public TimKiemCDDVDUI(String title){
		this.setTitle(title);
		addControls();
		addEvents();
		ketNoiCoSoDuLieu();
	}
	
	private void ketNoiCoSoDuLieu() {
		// TODO Auto-generated method stub
		try{
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			String connectionUrl = 
					"jdbc:sqlserver://TRINHANNGUYEN\\SQLEXPRESS:1433; "
					+ "databaseName=dbQlCDDVD; integratedSecurity = true;";

			conn = DriverManager.getConnection(connectionUrl);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}


	private void addEvents() {
		// TODO Auto-generated method stub
		btnTim.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				xuLyTim();
			}
		});
		
		
	}

	protected void xuLyTim() {
		// TODO Auto-generated method stub
		try{
			
			CallableStatement callableStatement = 
					conn.prepareCall("{call LayDanhSachDiaVaNamXuatBanBatKy(?,?)}");
			callableStatement.setString(1, txtLoaiCD.getText());
			callableStatement.setInt(2, Integer.parseInt(txtNamXuatBan.getText()));
			resultSet = callableStatement.executeQuery();
			
			/*String sql = "Select * from CDDVDCollection where loaidia = ?";
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1,txtLoaiCD.getText());
			resultSet = preparedStatement.executeQuery();*/
			
			dtmCd.setRowCount(0);
			while(resultSet.next()){ 
				Vector<Object> vec = new Vector<>();
				vec.add(resultSet.getString("Ma"));
				vec.add(resultSet.getString("TieuDe"));
				vec.add(resultSet.getString("LoaiDia"));
				vec.add(resultSet.getString("NamXuatBan"));
				dtmCd.addRow(vec);
			}
			
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
	}

	private void addControls() {
		// TODO Auto-generated method stub
		Container con = getContentPane();
		con.setLayout(new BorderLayout());
		
		dtmCd = new DefaultTableModel();
		dtmCd.addColumn("Mã ");
		dtmCd.addColumn("Tên");
		dtmCd.addColumn("Loại");
		dtmCd.addColumn("Năm xuất bản");
		tblCd = new JTable(dtmCd);
		JScrollPane sc = new JScrollPane(tblCd,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		con.add(sc, BorderLayout.CENTER);
		
		JPanel pnTim = new JPanel();
		JLabel lblTim = new JLabel("Nhập dữ liệu tìm loại CD/DVD");
		txtLoaiCD = new JTextField(10);
		btnTim = new JButton("Tìm");
		pnTim.add(lblTim);
		pnTim.add(txtLoaiCD);
		
		JLabel lblNxb = new JLabel("Năm xuất bản");
		txtNamXuatBan = new JTextField(10);
		pnTim.add(lblNxb);
		pnTim.add(txtNamXuatBan);
		
		pnTim.add(btnTim);
		con.add(pnTim, BorderLayout.NORTH);
	}
	public void showWindow(){
		this.setSize(600,300);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setModal(true);
		this.setVisible(true);
	}

}

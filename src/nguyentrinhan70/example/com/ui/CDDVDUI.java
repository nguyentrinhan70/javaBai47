package nguyentrinhan70.example.com.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class CDDVDUI extends JFrame {

	JTextField txtMa, txtTen, txtLoai, txtNamXuatBan;
	JButton btnLuu, btnTimKiem, btnXoa;

	DefaultTableModel dtmCd;
	JTable tblCd;

	Connection conn= null;
	PreparedStatement preparedStatement = null;
	ResultSet resultSet = null;

	public CDDVDUI(String title) {
		super(title);
		addControls();
		addEvents();
		ketNoiCoSoDuLieu();
		hienThiToanBoCDDVD();
	}

	public void hienThiChiTiet(String ma){
		try{
			String sql = "select * from CDDVDCollection where ma = ?";
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, ma);
			ResultSet rs = preparedStatement.executeQuery();
			if(rs.next()){
				txtMa.setText(rs.getString(1));
				txtTen.setText(rs.getString(2));
				txtLoai.setText(rs.getString(3));
				txtNamXuatBan.setText(rs.getInt(4)+"");
			}
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
	}
	public boolean kiemTraMaTonTai(String ma){
		try{
			String sql = "select * from CDDVDCollection where ma = ?";
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, ma);
			ResultSet rs = preparedStatement.executeQuery();
			return rs.next();
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return false;
	}
	private void hienThiToanBoCDDVD() {
		// TODO Auto-generated method stub
		try{

			CallableStatement callableStatement = conn.prepareCall("{ call LayToanBoCDVaDVD}");
			resultSet = callableStatement.executeQuery();

			
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

	private void ketNoiCoSoDuLieu() {
		// TODO Auto-generated method stub
		try{
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			String connectionUrl = 
					//jdbc:sqlserver://DESKTOP-71M73KO\\SQLEXPRESS:1433
					"jdbc:sqlserver://TRINHANNGUYEN\\SQLEXPRESS:1433; "
					+ "databaseName=dbQlCDDVD; integratedSecurity = true;";

			conn = DriverManager.getConnection(connectionUrl);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	private void addEvents() {
		// TODO Auto-generated method stub
		btnXoa.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				xuLyXoa();
			}
		});
		btnTimKiem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				TimKiemCDDVDUI ui = new TimKiemCDDVDUI("Tìm kiếm CD và DVD	");
				ui.showWindow();
			}
		});
		btnLuu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				xuLyLuuCDDVD();
			}
		});
		
		tblCd.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
			
					
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				int row = tblCd.getSelectedRow();
				if(row ==-1) return;
				String ma = tblCd.getValueAt(row, 0)+"";
				hienThiChiTiet(ma);	
				
			}
		});
		/*btnXoa.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				xuLyXoa();
				
			}
		});
*/
	}

	protected void xuLyXoa() {
		// TODO Auto-generated method stub
		if(kiemTraMaTonTai(txtMa.getText())==false){
			ImageIcon icon = new ImageIcon("hinhanh/loixoa.png");
			JOptionPane.showMessageDialog(null,
					"Mã không tồn tại ","Thông báo", 
					JOptionPane.OK_OPTION, icon);
			return;
		}
		else{
			ImageIcon icon = new ImageIcon("hinhanh/question.png");
			int ret = JOptionPane.showConfirmDialog(null, 
						"Bạn có muốn chắc chắn xóa " + txtMa.getText() + "  ưnày không", 
						"Thông báo", 
						JOptionPane.YES_NO_OPTION, 
						JOptionPane.QUESTION_MESSAGE, icon);
			
			if(ret==JOptionPane.YES_OPTION){
				try{
					String sql = "delete from CDDVDCollection where ma =?";
					preparedStatement = conn.prepareStatement(sql);
					preparedStatement.setString(1, txtMa.getText());
					int x = preparedStatement.executeUpdate();
					if(x>0){
						hienThiToanBoCDDVD();
					}
				}catch(Exception ex){
					ex.printStackTrace();
				}
				
			}
			
		}
	}

	protected void xuLyLuuCDDVD() {
		// TODO Auto-generated method stub
		if(kiemTraMaTonTai(txtMa.getText())){
			int ret =JOptionPane.showConfirmDialog(null, 
					"Mã " + txtMa.getText() + " Đã tồn tại Bạn có muốn cập nhật không",
					"Thông báo" ,JOptionPane.YES_NO_CANCEL_OPTION);
			if(ret ==JOptionPane.NO_OPTION) return;
			else{
				try{
					String sql = "update CDDVDCollection set TieuDe =?,"
							+ "LoaiDia = ?, NamXuatBan =? where ma = ?";
					preparedStatement = conn.prepareStatement(sql);
					preparedStatement.setString(1, txtTen.getText());
					preparedStatement.setString(2, txtLoai.getText());
					preparedStatement.setInt(3, Integer.parseInt(txtNamXuatBan.getText()));
					preparedStatement.setString(4, txtMa.getText());
					int x = preparedStatement.executeUpdate();
					if(x>0){
						hienThiToanBoCDDVD();
					}
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
		}
		else{
			try
			{
				String sql = "insert into CDDVDCollection values(?, ?,?,?)";
				preparedStatement = conn.prepareStatement(sql);
				preparedStatement.setString(1, txtMa.getText());
				preparedStatement.setString(2, txtTen.getText());
				preparedStatement.setString(3, txtLoai.getText());
				preparedStatement.setInt(4, Integer.parseInt(txtNamXuatBan.getText()));
				int x = preparedStatement.executeUpdate();
				if (x>0){
					hienThiToanBoCDDVD();
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}
	private void addControls() {
		// TODO Auto-generated method stub
		Container con = getContentPane();
		con.setLayout(new BorderLayout());

		JPanel pnThongTin = new JPanel();
		pnThongTin.setLayout(new BoxLayout(pnThongTin, BoxLayout.Y_AXIS));
		con.add(pnThongTin, BorderLayout.NORTH);

		JPanel pnMa = new JPanel();
		pnMa.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblMa = new JLabel("Mã");
		txtMa = new JTextField(20);
		pnMa.add(lblMa);
		pnMa.add(txtMa);
		pnThongTin.add(pnMa);

		JPanel pnTen = new JPanel();
		pnTen.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblTen = new JLabel("Tên");
		txtTen = new JTextField(20);
		pnTen.add(lblTen);
		pnTen.add(txtTen);
		pnThongTin.add(pnTen);

		JPanel pnLoai = new JPanel();
		pnLoai.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblLoai = new JLabel("Loại");
		txtLoai = new JTextField(20);
		pnLoai.add(lblLoai);
		pnLoai.add(txtLoai);
		pnThongTin.add(pnLoai);

		JPanel pnNxb = new JPanel();
		pnNxb.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblNxb = new JLabel("Năm xuất bản");
		txtNamXuatBan = new JTextField(20);
		pnNxb.add(lblNxb);
		pnNxb.add(txtNamXuatBan);
		pnThongTin.add(pnNxb);

		lblMa.setPreferredSize(lblNxb.getPreferredSize());

		lblTen.setPreferredSize(lblNxb.getPreferredSize());
		lblLoai.setPreferredSize(lblNxb.getPreferredSize());

		JPanel pnButton = new JPanel();
		pnButton.setLayout(new FlowLayout(FlowLayout.LEFT));
		btnLuu = new JButton("Lưu");
		btnXoa = new JButton("Xóa");
		btnTimKiem = new JButton("Tìm kiếm");

		pnButton.add(btnLuu);
		pnButton.add(btnXoa);
		pnButton.add(btnTimKiem);

		pnThongTin.add(pnButton);

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

	}
	public void showWindow(){
		this.setSize(500, 500);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
}

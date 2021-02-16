import java.sql.*;
import java.util.Scanner;

public class Project {
	public static void main(String[] args) {
		String DB_URL="jdbc:oracle:thin:@localhost:1521:orcl";
		String DB_USER="ryu";
		String DB_PASSWORD="1234";
		
		Connection con=null;
		String query=null;
		Statement stmt=null;
		ResultSet rs=null;
		CallableStatement cstmt = null;
		PreparedStatement pstmt = null;
		String callname = null;
		Scanner scan = new Scanner(System.in);
		int menu = 0, songId = 0;
		String result = null, id = null, pwd = null, birth = null, title = null, artist = null;
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			System.out.println("Driver Loading Success");
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			con=DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
			System.out.println("Database Connection Success");
			
			while(true) {
				System.out.println("--------------------------");
				System.out.println("�Ʒ� �޴� �� ���ϴ� �޴���ȣ�� �Է��ϼ���");
				System.out.println("1. �α���");
				System.out.println("2. ȸ������");
				System.out.println("3. ���α׷� ����");
				System.out.println("--------------------------");
				menu = scan.nextInt();
				scan.nextLine();
				
				if(menu==1) {	//�α���
					System.out.println("���̵� �Է��ϼ���.");
					id = scan.nextLine();
					query = "SELECT * FROM member WHERE id = ?";
					pstmt = con.prepareStatement(query);
					pstmt.setString(1,id);
					rs = pstmt.executeQuery();
					
					if(rs.next() == false)
						System.out.println("�������� �ʴ� ���̵��Դϴ�.");
					else {
						System.out.println("��й�ȣ�� �Է��ϼ���.");
						pwd = scan.nextLine();
						
						query = "SELECT * FROM member WHERE id = ? AND pwd = ?";
						pstmt = con.prepareStatement(query);
						pstmt.setString(1, id);
						pstmt.setString(2, pwd);
						rs = pstmt.executeQuery();
						
						if(rs.next() == false)
							System.out.println("�߸��� ��й�ȣ�Դϴ�.");
						else {
							System.out.println("�α��εǾ����ϴ�.");
							break;
						}
					}
				}
				else if(menu==2) {	//ȸ������
					System.out.println("���̵� �Է��ϼ���.");
					id = scan.nextLine();
					query = "SELECT * FROM member WHERE id = ?";
					pstmt = con.prepareStatement(query);
					pstmt.setString(1,id);
					rs = pstmt.executeQuery();
					if(rs.next()==true)
						System.out.println("�̹� ������� ���̵� �Դϴ�.");
					else {
						System.out.println("��й�ȣ�� �Է��ϼ���.");
						pwd = scan.nextLine();
						System.out.println("��������� �Է��ϼ���.");
						birth = scan.nextLine();
				
						callname = "{call insert_member(?,?,?,?)}";
						cstmt = con.prepareCall(callname);
						cstmt.setString(1, id);
						cstmt.setString(2, pwd);
						cstmt.setString(3, birth);
						cstmt.registerOutParameter(4, java.sql.Types.VARCHAR);
						cstmt.execute();
						result = cstmt.getString(4);
						System.out.println(result);
					}
				}
				else if(menu==3) {  //���α׷� ����
					System.out.println("���α׷��� ����Ǿ����ϴ�.");
					return;
				}
				else
					System.out.println("�޴� ��ȣ�� �߸� �Է��ϼ̽��ϴ�.");
			}
			
			if(id.equals("admin")) { //������ �α���
				System.out.println("�ȳ��ϼ��� �����ڴ�.");
				while(true) {
					System.out.println("--------------------------");
					System.out.println("�Ʒ� �޴� �� ���ϴ� �޴���ȣ�� �Է��ϼ���");
					System.out.println("1. �뷡 ���");
					System.out.println("2. ȸ�� ����Ʈ");
					System.out.println("3. ���α׷� ����");
					System.out.println("--------------------------");
					menu = scan.nextInt();
					scan.nextLine();
					
					if(menu == 1) { //�뷡 �߰�
						System.out.println("������ �Է��ϼ���.");
						title = scan.nextLine();
						System.out.println("��Ƽ��Ʈ�� �Է��ϼ���.");
						artist = scan.nextLine();
						
						callname = "{call insert_song(?,?,?)}";
						cstmt = con.prepareCall(callname);
						cstmt.setString(1, title);
						cstmt.setString(2, artist);
						cstmt.registerOutParameter(3, java.sql.Types.VARCHAR);
						cstmt.execute();
						result = cstmt.getString(3);
						System.out.println(result);
					}
					else if(menu == 2) { //ȸ�� ����Ʈ ���
						System.out.println("ID       PWD        BIRTH");
						query = "SELECT * FROM member";
						stmt = con.createStatement();
						rs = stmt.executeQuery(query);
						while(rs.next())
							System.out.println(rs.getString(1)+"     "+rs.getString(2)+"     "+rs.getString(3));
					}
					else if(menu==3) {  //���α׷� ����
						System.out.println("���α׷��� ����Ǿ����ϴ�.");
						return;
					}
					else
						System.out.println("�޴� ��ȣ�� �߸� �Է��ϼ̽��ϴ�.");
				}
			}
			else { //ȸ�� �α���
				System.out.println("ȯ���մϴ� "+id+"��.");
				while(true) {
					System.out.println("--------------------------");
					System.out.println("�Ʒ� �޴� �� ���ϴ� �޴���ȣ�� �Է��ϼ���");
					System.out.println("1. �뷡 �˻�");
					System.out.println("2. ���� �˻�");
					System.out.println("3. �α���Ʈ"); //�α���Ʈ - hit ���� ū ����
					System.out.println("4. �÷��̸���Ʈ ����");
					System.out.println("5. ���α׷� ����");
					System.out.println("--------------------------");
					menu = scan.nextInt();
					scan.nextLine();
					
					if(menu == 1) {
						System.out.println("������ �Է��ϼ���.");
						title = scan.nextLine();
						
						System.out.println("��ȣ       ����        ��Ƽ��Ʈ");
						query = "SELECT songId, title, artist FROM song WHERE title=?";
						pstmt = con.prepareStatement(query);
						pstmt.setString(1, title);
						rs = pstmt.executeQuery();
						while(rs.next())
							System.out.println(rs.getString("songId")+"     "+rs.getString("title")+"     "+rs.getString("artist"));
					}
					else if(menu ==2) {
						System.out.println("������ �Է��ϼ���.");
						artist = scan.nextLine();
						
						System.out.println("��ȣ       ����        ��Ƽ��Ʈ");
						query = "SELECT songId, title, artist FROM song WHERE artist=?";
						pstmt = con.prepareStatement(query);
						pstmt.setString(1, artist);
						rs = pstmt.executeQuery();
						while(rs.next())
							System.out.println(rs.getString("songId")+"     "+rs.getString("title")+"     "+rs.getString("artist"));
					}
					else if(menu ==3) { //�α���Ʈ ����
						System.out.println("��ȣ       ����        ��Ƽ��Ʈ");
						query = "select * from song where ROWNUM<=20 Order by hit DESC, title ASC";
						stmt = con.createStatement();
						rs = stmt.executeQuery(query);
						while(rs.next())
							System.out.println(rs.getInt("songId")+"     "+rs.getString("title")+"     "+rs.getString("artist"));
					}
					else if(menu ==4) { //�÷��̸���Ʈ ����
						while(true) {
							System.out.println("----------���� �÷��̸���Ʈ----------");
							System.out.println("��ȣ       ����        ��Ƽ��Ʈ");
							query = "SELECT s.songId, s.title, s.artist FROM song s, playlist p WHERE s.songId=p.songId AND p.id=?";
							pstmt = con.prepareStatement(query);
							pstmt.setString(1, id);
							rs = pstmt.executeQuery();
							while(rs.next())
								System.out.println(rs.getString("songId")+"     "+rs.getString("title")+"     "+rs.getString("artist"));
							System.out.println("���ϴ� �޴��� �����ϼ���: 1. �뷡 �߰�       2. �뷡 ����       3. ������");
							menu = scan.nextInt();
							scan.nextLine();
							
							if(menu == 1) { //�÷��̸���Ʈ�� �뷡 �߰� - insert_playlist ���ν��� ���
								System.out.println("�߰��� �뷡 ��ȣ�� �Է��ϼ���.");
								songId= scan.nextInt();
								scan.nextLine();
				
								callname = "{call insert_playlist(?,?,?)}";
								cstmt = con.prepareCall(callname);
								cstmt.setString(1, id);
								cstmt.setInt(2, songId);
								cstmt.registerOutParameter(3, java.sql.Types.VARCHAR);
								cstmt.execute();
								result = cstmt.getString(3);
								System.out.println(result);
							}
							else if(menu == 2) { //�÷��̸���Ʈ�� �뷡 ���� - delete_playlist ���ν��� ���
								System.out.println("������ �뷡 ��ȣ�� �Է��ϼ���.");
								songId= scan.nextInt();
								scan.nextLine();
						
								callname = "{call delete_playlist(?,?,?)}";
								cstmt = con.prepareCall(callname);
								cstmt.setString(1, id);
								cstmt.setInt(2, songId);
								cstmt.registerOutParameter(3, java.sql.Types.VARCHAR);
								cstmt.execute();
								result = cstmt.getString(3);
								System.out.println(result);
							}
							else if(menu ==3) //�÷��̸���Ʈ ���� ���� ������
								break;
							else
								System.out.println("�޴� ��ȣ�� �߸� �Է��ϼ̽��ϴ�.");
						}
					}
					else if(menu == 5) {  //���α׷� ����
						System.out.println("���α׷��� ����Ǿ����ϴ�.");
						return;
					}
					else
						System.out.println("�޴� ��ȣ�� �߸� �Է��ϼ̽��ϴ�.");
				}
			}
		} catch(SQLException e) {e.printStackTrace();}
	}
}

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
				System.out.println("아래 메뉴 중 원하는 메뉴번호를 입력하세요");
				System.out.println("1. 로그인");
				System.out.println("2. 회원가입");
				System.out.println("3. 프로그램 종료");
				System.out.println("--------------------------");
				menu = scan.nextInt();
				scan.nextLine();
				
				if(menu==1) {	//로그인
					System.out.println("아이디를 입력하세요.");
					id = scan.nextLine();
					query = "SELECT * FROM member WHERE id = ?";
					pstmt = con.prepareStatement(query);
					pstmt.setString(1,id);
					rs = pstmt.executeQuery();
					
					if(rs.next() == false)
						System.out.println("존재하지 않는 아이디입니다.");
					else {
						System.out.println("비밀번호를 입력하세요.");
						pwd = scan.nextLine();
						
						query = "SELECT * FROM member WHERE id = ? AND pwd = ?";
						pstmt = con.prepareStatement(query);
						pstmt.setString(1, id);
						pstmt.setString(2, pwd);
						rs = pstmt.executeQuery();
						
						if(rs.next() == false)
							System.out.println("잘못된 비밀번호입니다.");
						else {
							System.out.println("로그인되었습니다.");
							break;
						}
					}
				}
				else if(menu==2) {	//회원가입
					System.out.println("아이디를 입력하세요.");
					id = scan.nextLine();
					query = "SELECT * FROM member WHERE id = ?";
					pstmt = con.prepareStatement(query);
					pstmt.setString(1,id);
					rs = pstmt.executeQuery();
					if(rs.next()==true)
						System.out.println("이미 사용중인 아이디 입니다.");
					else {
						System.out.println("비밀번호를 입력하세요.");
						pwd = scan.nextLine();
						System.out.println("생년월일을 입력하세요.");
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
				else if(menu==3) {  //프로그램 종료
					System.out.println("프로그램이 종료되었습니다.");
					return;
				}
				else
					System.out.println("메뉴 번호를 잘못 입력하셨습니다.");
			}
			
			if(id.equals("admin")) { //관리자 로그인
				System.out.println("안녕하세요 관리자님.");
				while(true) {
					System.out.println("--------------------------");
					System.out.println("아래 메뉴 중 원하는 메뉴번호를 입력하세요");
					System.out.println("1. 노래 등록");
					System.out.println("2. 회원 리스트");
					System.out.println("3. 프로그램 종료");
					System.out.println("--------------------------");
					menu = scan.nextInt();
					scan.nextLine();
					
					if(menu == 1) { //노래 추가
						System.out.println("제목을 입력하세요.");
						title = scan.nextLine();
						System.out.println("아티스트를 입력하세요.");
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
					else if(menu == 2) { //회원 리스트 출력
						System.out.println("ID       PWD        BIRTH");
						query = "SELECT * FROM member";
						stmt = con.createStatement();
						rs = stmt.executeQuery(query);
						while(rs.next())
							System.out.println(rs.getString(1)+"     "+rs.getString(2)+"     "+rs.getString(3));
					}
					else if(menu==3) {  //프로그램 종료
						System.out.println("프로그램이 종료되었습니다.");
						return;
					}
					else
						System.out.println("메뉴 번호를 잘못 입력하셨습니다.");
				}
			}
			else { //회원 로그인
				System.out.println("환영합니다 "+id+"님.");
				while(true) {
					System.out.println("--------------------------");
					System.out.println("아래 메뉴 중 원하는 메뉴번호를 입력하세요");
					System.out.println("1. 노래 검색");
					System.out.println("2. 가수 검색");
					System.out.println("3. 인기차트"); //인기차트 - hit 값이 큰 순서
					System.out.println("4. 플레이리스트 편집");
					System.out.println("5. 프로그램 종료");
					System.out.println("--------------------------");
					menu = scan.nextInt();
					scan.nextLine();
					
					if(menu == 1) {
						System.out.println("제목을 입력하세요.");
						title = scan.nextLine();
						
						System.out.println("번호       제목        아티스트");
						query = "SELECT songId, title, artist FROM song WHERE title=?";
						pstmt = con.prepareStatement(query);
						pstmt.setString(1, title);
						rs = pstmt.executeQuery();
						while(rs.next())
							System.out.println(rs.getString("songId")+"     "+rs.getString("title")+"     "+rs.getString("artist"));
					}
					else if(menu ==2) {
						System.out.println("가수를 입력하세요.");
						artist = scan.nextLine();
						
						System.out.println("번호       제목        아티스트");
						query = "SELECT songId, title, artist FROM song WHERE artist=?";
						pstmt = con.prepareStatement(query);
						pstmt.setString(1, artist);
						rs = pstmt.executeQuery();
						while(rs.next())
							System.out.println(rs.getString("songId")+"     "+rs.getString("title")+"     "+rs.getString("artist"));
					}
					else if(menu ==3) { //인기차트 보기
						System.out.println("번호       제목        아티스트");
						query = "select * from song where ROWNUM<=20 Order by hit DESC, title ASC";
						stmt = con.createStatement();
						rs = stmt.executeQuery(query);
						while(rs.next())
							System.out.println(rs.getInt("songId")+"     "+rs.getString("title")+"     "+rs.getString("artist"));
					}
					else if(menu ==4) { //플레이리스트 편집
						while(true) {
							System.out.println("----------현재 플레이리스트----------");
							System.out.println("번호       제목        아티스트");
							query = "SELECT s.songId, s.title, s.artist FROM song s, playlist p WHERE s.songId=p.songId AND p.id=?";
							pstmt = con.prepareStatement(query);
							pstmt.setString(1, id);
							rs = pstmt.executeQuery();
							while(rs.next())
								System.out.println(rs.getString("songId")+"     "+rs.getString("title")+"     "+rs.getString("artist"));
							System.out.println("원하는 메뉴를 선택하세요: 1. 노래 추가       2. 노래 삭제       3. 나가기");
							menu = scan.nextInt();
							scan.nextLine();
							
							if(menu == 1) { //플레이리스트에 노래 추가 - insert_playlist 프로시저 사용
								System.out.println("추가할 노래 번호를 입력하세요.");
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
							else if(menu == 2) { //플레이리스트에 노래 삭제 - delete_playlist 프로시저 사용
								System.out.println("삭제할 노래 번호를 입력하세요.");
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
							else if(menu ==3) //플레이리스트 편집 에서 나가기
								break;
							else
								System.out.println("메뉴 번호를 잘못 입력하셨습니다.");
						}
					}
					else if(menu == 5) {  //프로그램 종료
						System.out.println("프로그램이 종료되었습니다.");
						return;
					}
					else
						System.out.println("메뉴 번호를 잘못 입력하셨습니다.");
				}
			}
		} catch(SQLException e) {e.printStackTrace();}
	}
}

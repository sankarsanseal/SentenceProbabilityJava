import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.BufferedReader;

import javax.imageio.stream.FileImageInputStream;

public class ReadFile {
	
	public static void main(String [] args)
	{

		
		java.sql.Connection con=null;
		java.sql.Statement stmt=null;
		java.sql.ResultSet rst=null;
		String inputline=null;
		String [] tokens=null;
		java.sql.PreparedStatement pstmt=null;
		String insertstr;
		String selectstr;
		String updatestr;
		String selectstrbi;
		String insertstrbi;
		String updatestrbi;
		String previousstr=null;
		String selecttotalstr;
		String allwordsstr;
		String selectedword=null;
		
		String updateprob=null;
		String updateprobbi=null;
		int i;
		int totalcount=1;
		int wordpcount=1;
		
		try
		{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con=DriverManager.getConnection("jdbc:mysql://localhost/ngram?useUnicode=true&characterEncoding=utf-8", "sanku", "testngram");
			/*stmt=con.createStatement();
			rst=stmt.executeQuery("SELECT * FROM monogram");
			System.out.println("Fetching Started");
			while(rst.next())
			{
				System.out.println(rst.getString("word"));
			}*/
			
			
			selectstr="SELECT * FROM monogram WHERE word=? LIMIT 1";
			insertstr="INSERT INTO monogram (word,count,probability) VALUES(?,?,?)";
			updatestr="UPDATE monogram SET count=count +1 WHERE word=?";
			selectstrbi="SELECT * FROM bigram WHERE wordp=? and wordn=? LIMIT 1";
			insertstrbi="INSERT INTO bigram (wordp, wordn, count , probability) VALUES(?,?,?,?)";
			updatestrbi="UPDATE bigram SET count=count+1 WHERE wordp=? and wordn=?";
			selecttotalstr="SELECT sum(count) FROM monogram";
			updateprob="UPDATE monogram SET probability=count/?";
			updateprobbi="UPDATE bigram SET probability=count/?  WHERE wordp=?";
			allwordsstr="SELECT word , count FROM monogram";
			//pstmt=con.prepareStatement(insertstr);
			
			
			File fileinput=new File("/Users/sankarsanseal/Downloads/bangladir/AB/wiki_37");
			BufferedReader in=new BufferedReader(new InputStreamReader(new FileInputStream(fileinput),"UTF-8"));
			while((inputline=in.readLine())!=null)
			{
				tokens=inputline.split("\\s+");
				for(i=0;i<tokens.length;i++)
				{
					System.out.println(tokens[i]);
					if(tokens[i]!=null && tokens[i].length() <=100)
					{
						pstmt=con.prepareStatement(selectstr);
						pstmt.setString(1,tokens[i]);
					
						rst=pstmt.executeQuery();
						if(rst.next())
						{
							pstmt.close();
							pstmt=con.prepareStatement(updatestr);
							pstmt.setString(1, tokens[i]);
							pstmt.executeUpdate();
							pstmt.close();
						
						}
						else
						{
						pstmt.close();
						pstmt=con.prepareStatement(insertstr);
						pstmt.setString(1,tokens[i] );
						pstmt.setInt(2, 1);
						pstmt.setFloat(3, 0);
						pstmt.executeUpdate();
						pstmt.close();
						}
						
						if(previousstr!=null)
						{
							pstmt.close();
							pstmt=con.prepareStatement(selectstrbi);
							pstmt.setString(1, previousstr);
							pstmt.setString(2, tokens[i]);
							rst=pstmt.executeQuery();
							if(rst.next())
							{
								pstmt.close();
								pstmt=con.prepareStatement(updatestrbi);
								pstmt.setString(1, previousstr);
								pstmt.setString(2, tokens[i]);
								pstmt.executeUpdate();
								pstmt.close();
							}
							else
							{
								pstmt.close();
								pstmt=con.prepareStatement(insertstrbi);
								pstmt.setString(1, previousstr);
								pstmt.setString(2, tokens[i]);
								pstmt.setInt(3, 1);
								pstmt.setDouble(4, 0);
								pstmt.executeUpdate();
								pstmt.close();
								
								
							}
						}
						previousstr=tokens[i];
					
					}
					
				}
				
				
				//con.commit();
			}
			
			if(pstmt!=null)
				pstmt.close();
				pstmt=con.prepareStatement(selecttotalstr);
				rst=pstmt.executeQuery();
				while(rst.next())
				{
					System.out.println("***"+rst.getInt(1));
					totalcount=rst.getInt(1);
					
				}
			if(pstmt!=null)
				pstmt.close();
				pstmt=con.prepareStatement(updateprob);
				pstmt.setInt(1, totalcount);
				pstmt.executeUpdate();
				
			if(stmt!=null)
				stmt.close();
				stmt=con.createStatement();
				rst=stmt.executeQuery(allwordsstr);
				while(rst.next())
				{
					selectedword=rst.getString(1);
					wordpcount=rst.getInt(2);
					if(pstmt!=null)
						pstmt.close();
					pstmt=con.prepareStatement(updateprobbi);
					pstmt.setInt(1,wordpcount);
					pstmt.setString(2, selectedword);
					pstmt.executeUpdate();
					
				}
			
			
			
			if(rst!=null)
			rst.close();
			if(stmt!=null)
			stmt.close();
			if(pstmt!=null)
			pstmt.close();
			con.close();
			in.close();
			
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		finally
		{

		}
	}
	

}

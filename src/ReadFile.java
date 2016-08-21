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
		java.sql.PreparedStatement pstmt;
		String insertstr;
		int i;
		try
		{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con=DriverManager.getConnection("jdbc:mysql://localhost/ngram?useUnicode=true&characterEncoding=utf-8", "sanku", "testngram");
			stmt=con.createStatement();
			rst=stmt.executeQuery("SELECT * FROM monogram");
			System.out.println("Fetching Started");
			while(rst.next())
			{
				System.out.println(rst.getString("word"));
			}
			
			insertstr="INSERT INTO monogram (word,count,probability) VALUES(?,?,?)";
			pstmt=con.prepareStatement(insertstr);
			
			
			File fileinput=new File("/home/sankarsan/Documents/banglawiki.txt/AB/wiki_00");
			BufferedReader in=new BufferedReader(new InputStreamReader(new FileInputStream(fileinput),"UTF-8"));
			while((inputline=in.readLine())!=null)
			{
				tokens=inputline.split("\\s+");
				for(i=0;i<tokens.length;i++)
				{
					System.out.println(tokens[i]);
					if(tokens[i]!=null)
					{
					pstmt.setString(1,tokens[i] );
					pstmt.setInt(2, 0);
					pstmt.setFloat(3, 0);
					pstmt.executeUpdate();
					}
					
				}
				//con.commit();
			}
			
			
			rst.close();
			stmt.close();
			pstmt.close();
			con.close();
			
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

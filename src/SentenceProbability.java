import java.util.Scanner;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;


public class SentenceProbability {
	
	public static void main(String [] args)
	{

		try
		{
			Scanner sc=new Scanner(System.in);
			String inputstr=null;
			String tokens[];
			
			String monogramprobstr=null;
			String bigramprobstr=null;
			String smoothprob;
			PreparedStatement pstmt=null;
			ResultSet rst;
			
			int i;
			double lambda=0.3;
			double sentenceprob=1;
			double condprob=0;
			double monoprob=0;
			
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost/ngram?useUnicode=true&characterEncoding=utf-8", "sanku", "testngram");
			monogramprobstr="SELECT probability FROM monogram WHERE word=?";
			bigramprobstr="SELECT probability FROM bigram WHERE wordp=? and wordn=?";	
			
			
			System.out.print("Enter the Senternce:");
			inputstr=sc.nextLine();
			
			tokens=inputstr.split("\\s+");
			
			for(i=0;i<tokens.length;i++)
			{
				
				if(i==0)
				{
					pstmt=con.prepareStatement(monogramprobstr);
					pstmt.setString(1, tokens[i]);

					
				}
				else
				{
					pstmt=con.prepareStatement(bigramprobstr);
					pstmt.setString(1, tokens[i-1]);
					pstmt.setString(2, tokens[i]);
					
				}
				
				rst=pstmt.executeQuery();
				if(rst.next())
				{
					condprob=rst.getDouble(1);
					rst.close();
				}
				else
				{
					if(pstmt!=null)
						pstmt.close();
						pstmt=con.prepareStatement(monogramprobstr);
						pstmt.setString(1, tokens[i]);
						rst=pstmt.executeQuery();
						if(rst.next())
						{
							monoprob=rst.getDouble(1);
							rst.close();
						}

						

				}
				sentenceprob*=((1-lambda)*condprob + lambda*monoprob);
			}
			
			System.out.println("Sentence probability:" + sentenceprob);
			if(pstmt!=null)
			pstmt.close();
			
		}
		catch(Exception e)
		{
			System.err.println(e);
			
		}
	}

}

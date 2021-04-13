package lijie.test.fileStream;

import java.io.*;

/**
 * 创建InputStream实例inp，并将其赋值为System类的in属性，定义为控制台输入流，从inp输入流中获取字节信息，
 * 用这些字节信息创建字符串，并将其在控制台上输出。
 * @author zch
 *
 */
public class InputMessage {
	public static void main(String[] args) throws IOException {
		InputStream inp =new FileInputStream(new File("C:\\Users\\23190\\Desktop\\ss.txt"));
		InputStreamReader inputStreamReader=new InputStreamReader(inp,"Utf-8");
		BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
		StringBuffer str =new StringBuffer();
		int xx;
		try {
			while((xx=bufferedReader.read()) != -1){
				//根据用户输入的信息创建字符串
				str=str.append((char)xx);
			}
			System.out.println(str);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			inp.close();		//关闭流
		}
		
	}
}


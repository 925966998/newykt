package com.ky.ykt.utils;


import java.io.*;
import java.nio.charset.Charset;

public class ForFile {
        //生成文件路径
        private static String path = "D:\\file\\";

        //文件路径+名称
        private static String filenameTemp;
        /**
         * 创建文件
         * @param fileName  文件名称
         * @param
         * @return  是否创建成功，成功则返回true
         */
        public static boolean createFile(String fileName){
            Boolean bool = false;
            filenameTemp = path+fileName+".txt";//文件路径+名称+文件类型
            File file = new File(filenameTemp);
            try {
                //如果文件不存在，则创建新的文件
                if(!file.exists()){
                    file.createNewFile();
                    bool = true;
                    System.out.println("success create file,the file is "+filenameTemp);
                    //创建文件成功后，写入内容到文件里
//                    writeFileContent(filenameTemp, filecontent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return bool;
        }

        /**
         * 向文件中写入内容
         * @param filepath 文件路径与名称
         * @param newstr  写入的内容
         * @return
         * @throws IOException
         */
        public static boolean writeFileContent(String filepath,String newstr) throws IOException {
            Boolean bool = false;
            byte[] bytes = newstr.getBytes(Charset.forName("GB18030"));
            String str4 = new String(bytes, "GB18030");
            String filein = str4;//新写入的行，换行

            String temp  = "";

            FileInputStream fis = null;
            InputStreamReader isr = null;
            BufferedReader br = null;
            FileOutputStream fos  = null;
            PrintWriter pw = null;
            try {
                File file = new File(filepath);//文件路径(包括文件名称)
                //将文件读入输入流
                fis = new FileInputStream(file);
                isr = new InputStreamReader(fis);
                br = new BufferedReader(isr);
                StringBuffer buffer = new StringBuffer();

                //文件原有内容
                for(int i=0;(temp =br.readLine())!=null;i++){
                    buffer.append(temp);
                    // 行与行之间的分隔符 相当于“\n”
                    buffer = buffer.append(System.getProperty("line.separator"));
                }
                buffer.append(filein);

                fos = new FileOutputStream(file);
                pw = new PrintWriter(fos);
                //pw = new PrintWriter(new OutputStreamWriter(fos, "GB18030"));
                String s = P_Sm4Util.encryptEcb("4A65463855397748464F4D6673325938", buffer.toString(),"GB18030");
                pw.write(s.toCharArray());
                //pw.write(buffer.toString().toCharArray());

                pw.flush();
                bool = true;
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }finally {
                //不要忘记关闭
                if (pw != null) {
                    pw.close();
                }
                if (fos != null) {
                    fos.close();
                }
                if (br != null) {
                    br.close();
                }
                if (isr != null) {
                    isr.close();
                }
                if (fis != null) {
                    fis.close();
                }
            }
            return bool;
        }

    /**
     * 向文件中写入内容
     * @param filepath 文件路径与名称
     * @param
     * @return
     * @throws IOException
     */
    public static String readFileContent(String filepath) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filepath), Charset.forName("GBK")));
        StringBuilder sb = new StringBuilder();
        String str;
        while((str = br.readLine()) != null){
            sb.append(str);
        }
        return sb.toString();
    }

        /**
         * 删除文件
         * @param fileName 文件名称
         * @return
         */
        public static boolean delFile(String fileName){
            Boolean bool = false;
            filenameTemp = path+fileName+".txt";
            File file  = new File(filenameTemp);
            try {
                if(file.exists()){
                    file.delete();
                    bool = true;
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
            return bool;
        }


    public static boolean writeFile(String filePathAndName, String fileContent) throws IOException {
        try {
            File f = new File(filePathAndName);
            if (!f.exists()) {
                f.createNewFile();
            }
            //
            OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(f), "GB18030");
            BufferedWriter writer = new BufferedWriter(write);
            writer.write(fileContent);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

//    public void toObject() throws UnsupportedEncodingException{
//        String str = "ab丁亦凝";//编译环境默认是utf8格式
//        byte[] bytes = str.getBytes(Charset.forName("GB18030"));//这一步就是转成gb18030格式的字节码
//        for (byte b : bytes)
//        {
//            System.out.print(b + " ");
//        }
//        //字节码转成gb18030的字符串
//        String str4 = new String(bytes, "GB18030");
//        System.out.println(str4);
//        System.out.println();
//
//        //再转回utf8
//        byte[] bytes4 = str4.getBytes(Charset.forName("UTF-8"));
//        for (byte b : bytes4)
//        {
//            System.out.print(b + " ");
//        }
//        System.out.println();
//        String str44 = new String(bytes4, "UTF-8");
//        System.out.println("---"+str44);//abc你好
//    }



}

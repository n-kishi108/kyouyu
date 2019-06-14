//
// Practice1_01: 1件のHTML文書を読み込み、その中に含まれる画像ファイル名と、
//               その画像の周辺テキスト（alt属性のテキスト、親ノードのテキスト）を出力する
//
import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.*;
import java.util.*;
import java.io.*;

public class Practice1_01 {
    DOMParser parser;           // DOMを解釈するパーザ
    String inputHtmlFileName;   // 読み込むHTMLファイル名
    String resultFileName;      // 出力ファイル名
    ArrayList<Result1> results; // 抽出結果を格納するリスト

    void run(String[] args) throws Exception{
        // 初期化
        initialize(args);
        // 該当箇所の抽出処理
        extractTargets();
        // 抽出結果の書き出し
        writeResult();
    }


    // 初期化するメソッド
    void initialize(String args[]) throws Exception{
        String resultDir = "../result/";
        // DOMを解釈するパーザを準備
        parser = new DOMParser();
        parser.setFeature("http://xml.org/sax/features/namespaces", false);
        // 引数があれば第１引数をinputHtmlFileNameにセット、なければ"test.html"をセット
        inputHtmlFileName = args.length>0 ? args[0] : "test.html";
        System.out.println("SOURCE URL: " + inputHtmlFileName);
        // 出力ファイル名を生成
        makeDir(resultDir);
        resultFileName = resultDir + getFileName(inputHtmlFileName) + ".image_and_text";
        System.out.println(resultFileName);
    }

    String getFileName(String filename) {
      File f = new File(filename);
      String basename = f.getName();
      return basename.substring(0,basename.lastIndexOf('.'));
    }

    void makeDir(String dir) throws Exception{
        File d = new File(dir);
        if (!d.exists()) {
            d.mkdir();
        }
    }

    // 該当箇所を抽出するメソッド
    void extractTargets() throws Exception{
        // 抽出結果を格納する変数の実体を生成
        results = new ArrayList<>();
        // inputHtmlFileNameのファイルをパーサで読み込み、parserにDOMツリーを生成
        parser.parse(inputHtmlFileName);
        // parser内のDOMツリーのドキュメントノードをdocumentにセットする
        Document document = parser.getDocument();
        // "img"をタグ名にもつ要素を全て取得する
        NodeList nodeList = document.getElementsByTagName("img");
        // 各要素について、画像ファイル名と周辺テキストを抽出する
        for(Integer i=0; i < nodeList.getLength(); i++){
            extractFileNameAndTexts(nodeList, i, results);
        }
    }

    // 指定された要素について、画像ファイル名と周辺テキストを抽出するメソッド
    void extractFileNameAndTexts(NodeList nodeList, Integer i, ArrayList<Result1> results){
        // 該当要素をElement型で取得する
        Element element = (Element)nodeList.item(i);
        // 結果を格納する変数の実体を生成する
        Result1 result = new Result1();
        // 画像ファイル名が適正であれば周辺テキストを抽出する
        if(extractSrcFileName(element, result)){
            System.out.println(result.srcName);
            // altテキストを抽出
            extractAltText(element, result);
            // 親ノードテキストを抽出
            extractParText(element, result);
            // 結果を格納するリストに抽出結果を追加
            results.add(result);
        }
    }

    // 画像ファイル名が適正であるか判断するメソッド
    Boolean extractSrcFileName(Element element, Result1 result){ // 画像ファイル名を抽出する
        // 画像ファイル名をsrcNameにセットする
        String srcName = element.getAttribute("src");
        System.out.println(element.getAttribute("src"));
        // 画像ファイル名がない、あるいは、先頭が"test_data"で始まらない場合は、適正なファイル名ではないと判断する
        if(srcName.length()<=0 || !srcName.startsWith("test_data")) {
            return false;
        }
        // 画像ファイル名の先頭に"../data"をつける
        srcName = "../data/" + srcName;
        // 画像ファイル名の抽出結果を格納
        result.srcName = srcName;
        // 適正なファイル名として判断
        return true;
    }

    // altテキストを抽出するメソッド
    void extractAltText(Element element, Result1 result){
        // altテキストをtextAltにセットする
        String textAlt = element.getAttribute("alt");
        // 該当する周辺テキストがない場合は"NONE"をセットする
        if(textAlt.isEmpty()) textAlt = "NONE";
        // altテキストの抽出結果を格納
        result.textAlt = textAlt;
    }

    // 親ノードテキストを抽出するメソッド
    void extractParText(Element element, Result1 result){
        // 親ノードのテキストをtextParにセットする
        Node nodeParent = element.getParentNode();
        Element elementParent = (Element)nodeParent;
        String textPar = elementParent.getTextContent();
        // 該当する周辺テキストがない場合は"NONE"をセットする
        if(textPar.isEmpty()) textPar = "NONE";
        // 親ノードテキストの抽出結果を格納
        result.textPar = textPar;
    }

    // 抽出結果を書き出すメソッド
    void writeResult() throws IOException{
        // 出力ファイルをオープン
        PrintWriter out = new PrintWriter(new FileWriter(resultFileName));
        // リストの各抽出結果を、所定の書式で出力する
        // ??????
        // System.out.println("sss:w");
        System.out.println(results);
        for(Result1 result : results){
//             out.printf("%s\t%s\t%s%n",result.srcName,result.textAlt,result.textPar);
            out.printf("%s\t%s\t%s",result.srcName,result.textAlt,result.textPar);
        }
        // 出力ファイルをクローズ
        out.close();
    }

    public static void main(String args[]) throws Exception{
        Practice1_01 app = new Practice1_01();
        app.run(args);
    }
}

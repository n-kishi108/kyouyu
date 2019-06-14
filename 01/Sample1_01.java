//
// Sample1_01: 与えられたHTML文書を読み込み、タイトルを表示する
//
import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.*;

public class Sample1_01 {
    DOMParser parser;           // DOMを解釈するパーザ
    String inputHtmlFileName;   // 読み込むHTMLファイル名

    void run(String[] args) throws Exception{
        // 初期化
        initialize(args);
        // HTML文書をパーサで読み込み、各要素のテキスト内容を表示する
        printTextContent();
    }

    // DOMパーザを初期化し、HTML文書のファイル名をセットするメソッド
    void initialize(String args[]) throws Exception{
        // DOMを解釈するパーザを準備
        parser = new DOMParser();
        parser.setFeature("http://xml.org/sax/features/namespaces", false);
        // 引数があれば第１引数をinputHtmlFileNameにセット、なければ"test.html"をセット
        inputHtmlFileName = args.length>0 ? args[0] : "test.html";
        System.out.println("SOURCE URL: " + inputHtmlFileName);
    }

    // HTML文書をパーサで読み込み、各要素のテキスト内容を表示するメソッド
    void printTextContent() throws Exception{
        try {
            // HTMLファイルをパーサで読み込み、parserにDOMツリーを生成
            parser.parse(inputHtmlFileName);
            // parser内のDOMツリーのドキュメントノードをdocumentにセットする
            Document document = parser.getDocument();
            // "title"をタグ名にもつ要素を全て取得する
            NodeList nodeList = document.getElementsByTagName("title");

            // 各要素のテキスト内容を表示する
            for(int i=0; i < nodeList.getLength(); i++){
                Element element = (Element)nodeList.item(i);
                System.out.println(element.getTextContent());
            }
        } catch(Exception e) { e.printStackTrace(); } // 例外発生時の状況を表示する
    }

    public static void main(String args[]) throws Exception{
        // インスタンスの作成と実行
        Sample1_01 app = new Sample1_01();
        app.run(args);
    }
}

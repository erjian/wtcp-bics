package cn.com.wanwei.bic.utils;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class IKAnalyzerUtils {

    public static List<String> cut(String text) throws IOException {
        StringReader sr = new StringReader(text);
        IKSegmenter ik = new IKSegmenter(sr, true);
        Lexeme lex = null;
        List<String> list = new ArrayList<>();
        while ((lex = ik.next()) != null) {
            list.add(lex.getLexemeText());
        }
        return list;
    }

    public static void main(String[] args) throws IOException {
        String text = "一个字的景区语气词有哇、呀、呢、喔、五泉山唉、吗、嗷、啊、嘞、哦、噢、呜、哼、哎、唉、啦";
        List<String> list = IKAnalyzerUtils.cut(text);
        System.out.println(list);
    }

}

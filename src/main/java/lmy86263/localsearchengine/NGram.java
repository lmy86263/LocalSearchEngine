package lmy86263.localsearchengine;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author lmy86263
 * @date 2018/08/22
 */
public class NGram {

    private int n = 2;
    private String source;
    private Pattern multiChinese = Pattern.compile("[\u4e00-\u9fa5]+");
    private Pattern singleChinese = Pattern.compile("[\u4e00-\u9fa5]");
    private Pattern multiEnglish = Pattern.compile("[a-zA-Z]*");
    private Pattern singleEnglish = Pattern.compile("[a-zA-Z]");

    public NGram(String source){
        this.source = source;
    }

    public NGram(int n, String source){
        this.n = n;
        try {
            this.source = new String(source.trim().getBytes("UTF-32"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public List<String> take(){
        List<String> items = new LinkedList<>();
        String[] tempResults = source.split("\\s");

        for(int i=0; i< tempResults.length; i++){
            String temp = tempResults[i];
            if(multiChinese.matcher(temp).matches()){
                items.addAll(takeChinese(temp));
            } else if(multiEnglish.matcher(temp).matches()){
                items.add(temp);
            } else{
                items.addAll(takeMixed(temp));
            }
        }
        return items;
    }

    public List<String> takeChinese(String src){
        List<String> chs = new LinkedList<>();
        for(int i=0; i<src.length()-n+1; i++){
            if(!ignoreChar(src.charAt(i))){
                chs.add(src.substring(i, i+n).trim());
            } else{
                i++;
            }
        }
        return chs;
    }

    public List<String> takeMixed(String src){
        List<String> mixedItems = new LinkedList<>();
        boolean isCh = false;
        StringBuilder builder = new StringBuilder();
        for(int i=0; i< src.length(); i++){
            if(!ignoreChar(src.charAt(i))){
                if(singleEnglish.matcher(String.valueOf(src.charAt(i))).find()){
                    if(isCh){
                        isCh = false;
                        if (builder.length() > 0) {
                            mixedItems.addAll(takeChinese(builder.toString()));
                            builder.delete(0, builder.length());
                        }
                    }
                    builder.append(src.charAt(i));
                    if(i == src.length() - 1){
                        mixedItems.add(builder.toString());
                    }
                }
                if(singleChinese.matcher(String.valueOf(src.charAt(i))).find()){
                    if(!isCh){
                        isCh = true;
                        if(builder.length() > 0){
                            mixedItems.add(builder.toString());
                            builder.delete(0, builder.length());
                        }
                    }
                    builder.append(src.charAt(i));
                    if(i == src.length() - 1){
                        mixedItems.addAll(takeChinese(builder.toString()));
                    }
                }
            }
        }
        return mixedItems;
    }

    /**
     * 忽略不参与构建索引的字符
     * @param ch 输入的字符（UTF-32）
     * @return 是否是空白字符
     */
    boolean ignoreChar(char ch)
    {
        switch (ch) {
            case ' ': case '\f': case '\n': case '\r': case '\t':
            case '!': case '"': case '#': case '$': case '%': case '&':
            case '\'': case '(': case ')': case '*': case '+': case ',':
            case '-': case '.': case '/':
            case ':': case ';': case '<': case '=': case '>': case '?': case '@':
            case '[': case '\\': case ']': case '^': case '_': case '`':
            case '{': case '|': case '}': case '~':
            case 0x3000: /* 全角空格 */
            case 0x3001: /* 、 */
            case 0x3002: /* 。 */
            case 0xFF08: /* （ */
            case 0xFF09: /* ） */
            case 0xFF01: /* ！ */
            case 0xFF0C: /* ， */
            case 0xFF1A: /* ： */
            case 0xFF1B: /* ； */
            case 0xFF1F: /* ? */
                return true;
            default:
                return false;
        }
    }
}

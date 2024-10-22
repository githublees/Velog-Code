import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Application {

    public static void main(String[] args) {

        // 욕심 수량자와 겸허 수량자
        String r1 = "(a*)(a*)";
        String r2 = "(a*?)(a*)";
        String r3 = "(a*)(a*?)";
        String r4 = "(a*?)(a*?)";

        String example_1 = "aaa";

        Matcher m1 = Pattern.compile(r1).matcher(example_1);
        Matcher m2 = Pattern.compile(r2).matcher(example_1);
        Matcher m3 = Pattern.compile(r3).matcher(example_1);
        Matcher m4 = Pattern.compile(r4).matcher(example_1);

        printMatchResult(m1);
        printMatchResult(m2);
        printMatchResult(m3);
        printMatchResult(m4);


        // 겸허 수량자의 사용 예시
        String re1 = "(\".*\")";
        String re2 = "(\".*?\")";
        String example_2 = "\"apple\", \"banana\", \"melon\"";

        Matcher m5 = Pattern.compile(re1).matcher(example_2);
        Matcher m6 = Pattern.compile(re2).matcher(example_2);

        if (m5.find()) {
            System.out.println("match[1] : " + m5.group(1));
        }

        int matchIdx = 1;
        while (m6.find()) {
            System.out.print("match[" + matchIdx + "] : " + m6.group() + " ");
            matchIdx++;
        }
        System.out.println();


        // 문자열 치환 예시
        String originalString = "The quick brown fox jumps over the lazy dog.";

        // 정규 표현식을 사용하여 "quick"을 "slow"로 치환
        String newStr = originalString.replaceAll("quick", "slow");

        System.out.println("Original: " + originalString);
        System.out.println("Modified: " + newStr);

        // 정규 표현식에서 $ 기능 사용하기
        String regexWithCapture = "(\\w+) fox";
        String replacementWithCapture = "$1 cat"; // $1은 첫 번째 캡처 그룹을 참조
        String modifiedWithCapture = originalString.replaceAll(regexWithCapture, replacementWithCapture);

        System.out.println("Modified with capture: " + modifiedWithCapture);

        String originalString_1 = "abc12345#$*%";

        // 정규 표현식: (nondigits)(digits)(non-alphanumerics)
        String regex = "^(?<alpha>[\\D]*)(?<number>\\d*)(?<special>[^\\w]*)$";

        // replaceAll을 사용하여 치환
        String newString = originalString_1.replaceAll(regex, "$1 - $2 - $3");
        String newString_1 = originalString_1.replaceAll(regex, "${alpha} - ${number} - ${special}");

        // 결과 출력
        System.out.println(newString); // abc - 12345 - #$*%
        System.out.println(newString_1); // abc - 12345 - #$*%
    }

    private static void printMatchResult(Matcher matcher) {
        if (matcher.find()) {
            String format = "%-15s %-10s\n";
            System.out.printf(format, "match[1] : " + matcher.group(1), "match[2] : " + matcher.group(2));
            return;
        }

        System.out.println("No match found");
    }
}

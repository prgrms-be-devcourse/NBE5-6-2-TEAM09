package com.grepp.codemap.interview.service;

import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class KeywordCompareService {

    // 개선된 불용어 리스트 (조사, 어미, 접속사 등) - HashSet으로 변경하여 중복 제거
    private final Set<String> stopwords = new HashSet<>(Arrays.asList(
            "은", "는", "이", "가", "을", "를", "에", "의", "에서", "로", "으로", "와", "과", "도", "만", "부터", "까지", "에게", "한테", "께", "에서부터",
            "하다", "입니다", "있다", "없다", "되다", "이다", "아니다", "그리고", "또한", "하지만", "그러나", "따라서", "그래서", "즉", "예를", "들면",
            "때문에", "경우", "통해", "위해", "위한", "대한", "같은", "다른", "모든", "각각", "여러", "많은", "적은", "크다", "작다", "좋다", "나쁘다",
            "할", "수", "것", "등", "및", "또는", "그", "저", "그것", "이것", "저것", "여기", "거기", "저기", "때", "중", "동안", "후", "전",
            "다시", "더", "매우", "정말", "진짜", "아주", "너무", "조금", "약간", "대략", "거의", "완전히", "전혀", "별로", "특히", "주로"
    ));

    // 조사 및 어미 패턴 (단어 뒤에 붙는 것들) - 길이순으로 정렬하여 긴 것부터 매칭
    private final Set<String> particles = new HashSet<>(Arrays.asList(
            "입니다", "습니다", "합니다", "였습니다", "했습니다", "됩니다", "있습니다", "없습니다",
            "이라도", "나마", "이나마", "든지", "이든지", "거나", "이거나", "에서부터", "처럼", "같이",
            "은", "는", "이", "가", "을", "를", "의", "에", "에서", "로", "으로", "와", "과",
            "도", "만", "부터", "까지", "에게", "한테", "께", "보다", "마저", "조차", "라도",
            "나", "이나", "하다", "되다", "이다", "였다", "했다", "이고", "고", "에요", "예요"
    ));

    // 개선된 키워드 추출 로직
    public List<String> extractKeywords(String modelAnswer) {
        if (modelAnswer == null || modelAnswer.trim().isEmpty()) {
            return new ArrayList<>();
        }

        // 문장 부호 제거 후 토큰화
        String cleanText = modelAnswer.replaceAll("[.!?,:;\"'()\\[\\]{}]", " ");
        String[] tokens = cleanText.split("\\s+");

        Set<String> keywords = new HashSet<>();

        for (String token : tokens) {
            // 특수문자 제거 (한글, 영문, 숫자만 남김)
            String cleanToken = token.replaceAll("[^가-힣a-zA-Z0-9]", "").trim();

            // 조사 제거
            cleanToken = removeParticles(cleanToken);

            // 길이 체크 및 불용어 필터링
            if (cleanToken.length() >= 2 && !stopwords.contains(cleanToken)) {
                // 영어의 경우 소문자로 변환
                if (cleanToken.matches(".*[a-zA-Z].*")) {
                    cleanToken = cleanToken.toLowerCase();
                }
                keywords.add(cleanToken);
            }
        }

        return new ArrayList<>(keywords);
    }

    /**
     * 단어에서 조사를 제거하는 메서드
     */
    private String removeParticles(String word) {
        if (word == null || word.length() <= 2) {
            return word;
        }

        // 단어 끝에서부터 조사를 찾아서 제거 (긴 조사부터 먼저 체크)
        List<String> sortedParticles = particles.stream()
                .sorted((a, b) -> Integer.compare(b.length(), a.length()))
                .collect(Collectors.toList());

        for (String particle : sortedParticles) {
            if (word.endsWith(particle) && word.length() > particle.length()) {
                String stem = word.substring(0, word.length() - particle.length());
                // 어간이 너무 짧지 않은 경우에만 조사 제거
                if (stem.length() >= 2) {
                    return stem;
                }
            }
        }

        return word;
    }

    // 키워드 매칭 정확도 계산 (조사 제거 적용)
    public double calculateAccuracy(String userAnswer, List<String> keywords) {
        if (keywords.isEmpty()) return 100.0;

        List<String> matchedKeywords = findMatchedKeywords(userAnswer, keywords);
        return (double) matchedKeywords.size() / keywords.size() * 100;
    }

    // 점수 기반 평가
    public String getGradeByAccuracy(double accuracy) {
        if (accuracy >= 90) return "A+";
        else if (accuracy >= 80) return "A";
        else if (accuracy >= 70) return "B+";
        else if (accuracy >= 60) return "B";
        else if (accuracy >= 50) return "C";
        else return "D";
    }

    // 평가 코멘트 생성
    public String getEvaluationComment(double accuracy, int totalKeywords, int matchedCount) {
        StringBuilder comment = new StringBuilder();

        if (accuracy >= 90) {
            comment.append("🎉 훌륭합니다! 핵심 내용을 모두 포함한 완벽한 답변입니다.");
        } else if (accuracy >= 70) {
            comment.append("👍 좋은 답변입니다! 대부분의 핵심 내용을 포함했습니다.");
        } else if (accuracy >= 50) {
            comment.append("📝 괜찮은 답변이지만 몇 가지 핵심 내용이 빠져있습니다.");
        } else {
            comment.append("📚 답변을 보완해보세요. 중요한 키워드들이 많이 누락되었습니다.");
        }

        comment.append(String.format(" (전체 %d개 중 %d개 포함, %.1f%%)",
                totalKeywords, matchedCount, accuracy));

        return comment.toString();
    }

    // 사용자 답변에 없는 키워드 반환 (정확한 단어 경계 매칭)
    public List<String> findMissingKeywords(String userAnswer, List<String> keywords) {
        List<String> missing = new ArrayList<>();

        for (String keyword : keywords) {
            if (!isKeywordPresent(userAnswer, keyword)) {
                missing.add(keyword);
            }
        }
        return missing;
    }

    // 사용자 답변에 포함된 키워드 반환 (정확한 단어 경계 매칭)
    public List<String> findMatchedKeywords(String userAnswer, List<String> keywords) {
        List<String> matched = new ArrayList<>();

        for (String keyword : keywords) {
            if (isKeywordPresent(userAnswer, keyword)) {
                matched.add(keyword);
            }
        }
        return matched;
    }

    /**
     * 키워드가 텍스트에 존재하는지 정확히 판단하는 메서드
     * 단어 경계를 고려하여 부분 매칭 문제 해결
     */
    private boolean isKeywordPresent(String text, String keyword) {
        if (text == null || keyword == null) {
            return false;
        }

        // 텍스트에서 키워드 추출
        List<String> textKeywords = extractKeywords(text);

        // 정확한 키워드 매칭 (대소문자 구분 없이)
        for (String textKeyword : textKeywords) {
            if (textKeyword.equalsIgnoreCase(keyword)) {
                return true;
            }
        }

        // 추가적으로 원본 텍스트에서도 단어 경계를 고려한 매칭
        String lowerText = text.toLowerCase();
        String lowerKeyword = keyword.toLowerCase();

        // 정규식을 사용한 단어 경계 매칭
        String pattern = "\\b" + lowerKeyword + "(?:[은는이가을를의에서로으로와과도만부터까지에게한테께처럼같이보다마저조차라도나이나거나이거나하다되다이다였다했다이고고에요예요입니다습니다합니다]*)\\b";
        return lowerText.matches(".*" + pattern + ".*");
    }

    // 모범 답안 하이라이팅 (포함된 키워드는 초록색, 누락된 키워드는 빨간색)
    public String generateHighlightedAnswer(String modelAnswer, List<String> matchedKeywords, List<String> missingKeywords) {
        String highlighted = modelAnswer;

        // 매칭된 키워드를 초록색으로 하이라이트 (조사가 붙은 형태도 함께 하이라이트)
        for (String keyword : matchedKeywords) {
            highlighted = highlightKeywordWithParticles(highlighted, keyword,
                    "<mark style=\"background-color:#d8f5d0; color:#2e7d32; font-weight:bold;\">$0</mark>");
        }

        // 누락된 키워드를 빨간색으로 하이라이트 (조사가 붙은 형태도 함께 하이라이트)
        for (String keyword : missingKeywords) {
            highlighted = highlightKeywordWithParticles(highlighted, keyword,
                    "<mark style=\"background-color:#ffe6e6; color:#d32f2f; font-weight:bold;\">$0</mark>");
        }

        return highlighted;
    }

    /**
     * 키워드와 그 키워드에 조사가 붙은 형태를 모두 하이라이트하는 메서드
     * 단어 경계를 고려하여 정확한 매칭
     */
    private String highlightKeywordWithParticles(String text, String keyword, String replacement) {
        // 정규식 패턴으로 단어 경계를 고려한 하이라이팅
        String lowerKeyword = keyword.toLowerCase();

        // 키워드 + 조사 패턴 생성
        String particlePattern = "(?:[은는이가을를의에서로으로와과도만부터까지에게한테께처럼같이보다마저조차라도나이나거나이거나하다되다이다였다했다이고고에요예요입니다습니다합니다]*)";
        String pattern = "\\b(" + lowerKeyword + particlePattern + ")\\b";

        // 대소문자 구분 없이 매칭하되, 원본 텍스트 유지
        return text.replaceAll("(?i)" + pattern, replacement.replace("$0", "$1"));
    }

    // 상세 분석 결과 생성
    public Map<String, Object> generateDetailedAnalysis(String userAnswer, List<String> keywords) {
        Map<String, Object> analysis = new HashMap<>();

        List<String> matchedKeywords = findMatchedKeywords(userAnswer, keywords);
        List<String> missingKeywords = findMissingKeywords(userAnswer, keywords);
        double accuracy = calculateAccuracy(userAnswer, keywords);

        analysis.put("totalKeywords", keywords.size());
        analysis.put("matchedKeywords", matchedKeywords);
        analysis.put("missingKeywords", missingKeywords);
        analysis.put("matchedCount", matchedKeywords.size());
        analysis.put("missingCount", missingKeywords.size());
        analysis.put("accuracy", accuracy);
        analysis.put("grade", getGradeByAccuracy(accuracy));
        analysis.put("comment", getEvaluationComment(accuracy, keywords.size(), matchedKeywords.size()));

        return analysis;
    }
}
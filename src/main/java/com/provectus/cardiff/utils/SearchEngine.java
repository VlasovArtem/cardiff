package com.provectus.cardiff.utils;

import com.provectus.cardiff.entities.DiscountCard;

import java.util.List;


/**
 * Created by blupashko on 24.09.2015.
 */
public class SearchEngine {

    public static List<DiscountCard> search(List<DiscountCard> allDiscountCards,
                                           String searchField, List<DiscountCard> searchFieldDiscountCards) {

        String inputTranslitereted = transliteration(searchField.toLowerCase());

        for (DiscountCard discountCard : allDiscountCards) {
            if (!searchFieldDiscountCards.contains(discountCard)
                    && (editdist(searchField.toLowerCase(), discountCard.getCompanyName().toLowerCase()) < 3
                    || editdist(inputTranslitereted.toLowerCase(), discountCard.getCompanyName().toLowerCase()) < 3))
                searchFieldDiscountCards.add(discountCard);
        }
        return searchFieldDiscountCards;
    }

    private static String transliteration(String text) {
        char[] abcInput = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'а', 'б', 'в', 'г', 'ґ', 'д', 'е', 'ё', 'є', 'ж', 'з', 'и', 'і', 'й', 'ї', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'ю', 'я'};
        String[] abcOutput = {"a", "б", "ц", "д", "е", "ф", "г", "ш", "и", "дж", "к", "л", "м", "н", "о", "п", "ку", "р", "с", "т", "у", "в", "в", "х", "й", "з", "a", "b", "v", "g", "g", "d", "e", "jo", "e", "zh", "z", "i", "i", "y", "y", "k", "l", "m", "n", "o", "p", "r", "s", "t", "u", "f", "h", "ts", "sh", "sch", "", "", "", "e", "ju", "ja"};

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            for (int x = 0; x < abcInput.length; x++)
                if (text.charAt(i) == abcInput[x]) {
                    builder.append(abcOutput[x]);
                }
        }
        return builder.toString();
    }

    private static int editdist(String S1, String S2) {
        int m = S1.length(), n = S2.length();
        int[] D1;
        int[] D2 = new int[n + 1];

        for (int i = 0; i <= n; i++)
            D2[i] = i;

        for (int i = 1; i <= m; i++) {
            D1 = D2;
            D2 = new int[n + 1];
            for (int j = 0; j <= n; j++) {
                if (j == 0) D2[j] = i;
                else {
                    int cost = (S1.charAt(i - 1) != S2.charAt(j - 1)) ? 1 : 0;
                    if (D2[j - 1] < D1[j] && D2[j - 1] < D1[j - 1] + cost)
                        D2[j] = D2[j - 1] + 1;
                    else if (D1[j] < D1[j - 1] + cost)
                        D2[j] = D1[j] + 1;
                    else
                        D2[j] = D1[j - 1] + cost;
                }
            }
        }
        return D2[n];
    }
}

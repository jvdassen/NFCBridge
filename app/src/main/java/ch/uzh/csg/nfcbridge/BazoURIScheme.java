package ch.uzh.csg.nfcbridge;

import java.nio.channels.UnsupportedAddressTypeException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BazoURIScheme {
    static Pattern basePattern = Pattern.compile("^https://bazopay2.surge.sh/#/");
    static Pattern existingBazoProtocol = Pattern.compile("bazo:");
    static Pattern bazoTransaction = Pattern.compile("paymentinfo=(.+)");



    public static String encodeAsBazoTransactionURI(String bazoaddress, String amount, String posid) {
        String baseString = "https://bazopay2.surge.sh/#/auth/user/send?";
        String result = baseString;

        if (bazoaddress.length() <= 0){
            throw new UnsupportedOperationException("The parameter bazoaddress is mandatory and must not be an empty string. Supplied: " + bazoaddress);
        }

        if (posid.length() > 0) {
            result += "posid=" + posid + "&";
        }
        result += "paymentinfo=" + "bazo:" + bazoaddress;
        if (amount.length() > 0) {
            result+= "?amount=" + amount;
        }
        return result;
    }
    public static TransactionDTO decodeFromTransactionURI(String encodedBazoTransaction) {
        System.out.println(encodedBazoTransaction);
        Matcher matchHostBase = basePattern.matcher(encodedBazoTransaction);
        Matcher matchExistingBazoProtocol = existingBazoProtocol.matcher(encodedBazoTransaction);
        Matcher paymentInfoGroup = bazoTransaction.matcher(encodedBazoTransaction);

        if (!matchHostBase.find()) {
            throw new UnsupportedOperationException("TransactionURI has a bad host: " + encodedBazoTransaction);
        };
        if (!matchExistingBazoProtocol.find()) {
            throw new UnsupportedOperationException("TransactionURI needs to have the Bazo keyword to indicate the protocol: " + encodedBazoTransaction);
        };
        Boolean foundPayment= paymentInfoGroup.find();

        if (foundPayment){
            System.out.println("group: " + paymentInfoGroup.group(1));
        }

        return null;
    }
}
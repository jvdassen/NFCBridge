package ch.uzh.csg.nfcbridge;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;


public class BazoURISchemeTest {
    String amount, bazoaddress, optionalPOSID, allParams, onlyAddressAndAmount, onlyAddress, onlyAmount, onlyPOSID;

    @Before
    public void setUp() throws Exception {
        amount = "3.5";
        bazoaddress = "2B4M98CAJ2ND1T54E89AS4598JD32KWU8A";
        optionalPOSID = "133";
        allParams = "https://oysy.surge.sh/#/auth/user/send?posid=133&paymentinfo=bazo:2B4M98CAJ2ND1T54E89AS4598JD32KWU8A?amount=3.5";
        onlyAddressAndAmount = "https://oysy.surge.sh/#/auth/user/send?paymentinfo=bazo:2B4M98CAJ2ND1T54E89AS4598JD32KWU8A?amount=3.5";
        onlyAddress = "https://oysy.surge.sh/#/auth/user/send?paymentinfo=bazo:2B4M98CAJ2ND1T54E89AS4598JD32KWU8A";
        onlyPOSID =  "https://oysy.surge.sh/#/auth/user/send?posid=133";
    }
    @Test
    public void completeEcodingTest() {
        String encodedAllParams = BazoURIScheme.encodeAsBazoTransactionURI(bazoaddress, amount, optionalPOSID);
        assertTrue(encodedAllParams.equals(allParams));
    }
    @Test
    public void partialEncodingTest() {
        String encodedOnlyAddressAndAmount = BazoURIScheme.encodeAsBazoTransactionURI(bazoaddress, amount, "");
        assertTrue(encodedOnlyAddressAndAmount.equals(onlyAddressAndAmount));

        String encodedOnlyAddress = BazoURIScheme.encodeAsBazoTransactionURI(bazoaddress, "", "");
        assertTrue(encodedOnlyAddress.equals(onlyAddress));
    }
    @Test(expected = UnsupportedOperationException.class)
    public void unsupportedEncodingTest() {
        String encodedOnlyPOSID = BazoURIScheme.encodeAsBazoTransactionURI( "", "", "133");

    }
    @Test
    public void decodingTest() {
        String encodedAllParams = BazoURIScheme.encodeAsBazoTransactionURI(bazoaddress, amount, optionalPOSID);
        BazoURIScheme.decodeFromTransactionURI(encodedAllParams);
    }
}
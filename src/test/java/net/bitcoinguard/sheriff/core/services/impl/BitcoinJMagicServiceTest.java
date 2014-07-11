package net.bitcoinguard.sheriff.core.services.impl;

import com.google.bitcoin.core.ECKey;
import com.google.bitcoin.core.Utils;
import com.google.bitcoin.script.Script;
import com.google.bitcoin.script.ScriptChunk;
import com.google.bitcoin.script.ScriptOpCodes;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class BitcoinJMagicServiceTest {
    BitcoinJMagicService bitcoinJMagicService = new BitcoinJMagicService();

    @Test
    public void testGenerateKeyPair() throws Exception {

        Map<String, String> stringStringMap = bitcoinJMagicService.generateKeyPair();

        ECKey key = ECKey.fromPrivateAndPrecalculatedPublic(Utils.HEX.decode(stringStringMap.get(BitcoinJMagicService.PRIVATE_KEY)), Utils.HEX.decode(stringStringMap.get(BitcoinJMagicService.PUBLIC_KEY)));

        String testMessageSignature = key.signMessage("testMessage");
        key.verifyMessage("testMessage", testMessageSignature);

    }

    @Test
    public void testCreateMultiSignatureRedeemScript() {
        List<String> keys = createKeyList();
        String multiSignatureRedeemScript = bitcoinJMagicService.createMultiSignatureRedeemScript(keys, 2);

        Script script = new Script(Utils.HEX.decode(multiSignatureRedeemScript));

        assertThat(script.isSentToMultiSig(), is(true));

        List<ScriptChunk> chunks = script.getChunks();
        assertThat(chunks.size(), is(6));
        ScriptChunk two = chunks.get(0);
        ScriptChunk pushData1 = chunks.get(1);
        ScriptChunk pushData2 = chunks.get(2);
        ScriptChunk pushData3 = chunks.get(3);
        ScriptChunk three = chunks.get(4);
        ScriptChunk checkMultiSignature = chunks.get(5);

        assertThat(two.data, is(nullValue())); //Data has to be null for the opcode to be considered code of operation
        assertThat(two.opcode, is(ScriptOpCodes.OP_2)); //First instruction says how many keys we need to sign the transaction in this case its 2 keys so that why OP_2
        assertThat(pushData1.data, is(equalTo(Utils.HEX.decode(keys.get(0)))));
        assertThat(pushData1.opcode, is(33));    //the 33 is probably length of the data

        assertThat(pushData2.data, is(equalTo(Utils.HEX.decode(keys.get(1)))));
        assertThat(pushData2.opcode, is(33));    //the 33 is probably length of the data
        assertThat(pushData3.data, is(equalTo(Utils.HEX.decode(keys.get(2)))));
        assertThat(pushData3.opcode, is(33));    //the 33 is probably length of the data

        assertThat(three.data, is(nullValue())); //Data has to be null for the opcode to be considered code of operation
        assertThat(three.opcode, is(ScriptOpCodes.OP_3)); //this instruction specifies how many keys have been in the list

        assertThat(checkMultiSignature.data, is(nullValue())); //Data has to be null for the opcode to be considered code of operation
        assertThat(checkMultiSignature.opcode, is(ScriptOpCodes.OP_CHECKMULTISIG)); //instruction to do multi sign
    }

    private List<String> createKeyList() {
        List<String> keys = new ArrayList<>();
        keys.add(bitcoinJMagicService.generateKeyPair().get(BitcoinJMagicService.PUBLIC_KEY));
        keys.add(bitcoinJMagicService.generateKeyPair().get(BitcoinJMagicService.PUBLIC_KEY));
        keys.add(bitcoinJMagicService.generateKeyPair().get(BitcoinJMagicService.PUBLIC_KEY));
        return keys;
    }

    @Test
    public void checkHashOfScript() {
        List<String> keys = createKeyList();
        String multiSignatureRedeemScript = bitcoinJMagicService.createMultiSignatureRedeemScript(keys, 2);
        Script script = new Script(Utils.HEX.decode(multiSignatureRedeemScript));

        byte[] scriptHash = Utils.sha256hash160(script.getProgram()); //we create the hash of the script
        String address = bitcoinJMagicService.getAddressFromRedeemScript(multiSignatureRedeemScript);
        byte[] addressDecoded = Utils.parseAsHexOrBase58(address);  //we decode the address provided
        byte firstByteOfAddress = -60; //This is the number 3 in normal addresses but is 2 in testnet that prefix the address
        byte[] addressWithoutFirstByte = Arrays.copyOfRange(addressDecoded, 1, addressDecoded.length);  //we take out the first character so that we can compare the hash

        assertThat(addressDecoded[0], is(firstByteOfAddress));  //first character has to be special character for the network
        assertThat(scriptHash, is(equalTo(addressWithoutFirstByte)));  //the rest should match

    }
}
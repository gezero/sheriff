sheriff
=======

Spring REST application for handle p2sh multiginature addresses


Check the proof of concept test in  src/integration/java/com.bitcoinj.wallet.MultisignatureTest

So far we are creating P2SH addresses and refreshing balance as new transactions came in.
We can also create new not signed transaction to spend the money.
We can also cosign transaction that already has one signature. The cosigning is not very secure yet, basically we sign
what we get.

For now you have to broadcast the transaction yourself.
 

To use this project, you need bitcoinJ - go to bitcoinJ github page, clone it and mvn install it. 

Then it should be enough to gradle run the project to start it up.
 
TODO:
* broadcast cosigned transaction

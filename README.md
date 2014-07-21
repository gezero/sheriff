sheriff
=======

Spring REST application for handle p2sh multiginature addresses


Check the proof of concept test in  src/integration/java/com.bitcoinj.wallet.MultisignatureTest

So far we are creating P2SH addresses and refreshing balance as new transactions came in.
We can also create new not signed transaction to spend the money.
 

To use this project, you need bitcoinJ - go to bitcoinj github page, clone it and mvn install it. 

Then it should be enough to gradle run the project to start it up.
 
TODO:
* cosign transaction
* broadcast cosigned transaction

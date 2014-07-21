sheriff
=======

Spring REST application for handle p2sh multiginature addresses


Check the proof of concept test in  src/integration/java/com.bitcoinj.wallet.MultisignatureTest

So far we are creating P2SH addresses and refreshing balance as new transactions came in.
 

To use this project, you need bitcoinJ - go to bitcoinj github page, clone it and mvn install it. 

Then it should be enough to gradle run the project to start it up.
 
TODO:
* create new output transaction
* cosign transaction
* broadcast cosigned transaction

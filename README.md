Sheriff
=======

Spring REST application for handling p2sh multiginature addresses.

Purpose
-------
This project exists because I wanted to test how the whole P2SH world works for myself. Please feel free to reuse the code as you please. I do not guarantie that it works. So far everything runs in TestNet.

Showcase
--------
Check the proof of concept test in  [MultisignatureTest.java](https://github.com/gezero/sheriff/blob/master/src/integration/java/com/bitcoinj/wallet/MultisignatureTest.java) if you just want to see how to use bitcoinJ for p2sh addresses. Check the integration test in [AddressWalletTest.java](https://github.com/gezero/sheriff/blob/master/src/integration/java/sheriff/AddressWalletTest.java) to find out more about how to check in more detail how to use this application.

What can be done
----------------
So far we are creating P2SH addresses and refreshing balance as new transactions came in.
We can also create new not signed transaction to spend the money.
We can also cosign transaction that already has one signature. The cosigning is not very secure yet, basically we sign
what we get.
 
TODO list
---------
* broadcast cosigned transaction
* Updating of the transactions data in db
* Use other than in memory DB fro storing data (the data is now lost in between restarts)
* Test the application on Real Bitcoin Network (Currnently we run against testnet)

Building
========
You need BitcoinJ, get the newest version from the [BitcoinJ github sources](https://github.com/bitcoinj/bitcoinj). Install them in local repo.
Than run `gradlew build`. This should build the project and letyou  to run it. 

Running the project
===================
If you successfully build the project you can run it using `java -jar build/libs/sheriff-0.0.1-SNAPSHOT.jar`.
Second option is to run it directly using the `gradlew run` command

Usage
=====
Create multisignature  address
------------------------------
Suppose you will create 2 keys yourself like this ones:
Example Key1:

* public: 02c8064852f9e5d30e10997cdc4da43238016eeefbc20b439a8eb136546992786c
* private: 6fb904346234af150ad3b8dfecdc09a8d95497aaa183bcd629fca363fcd22edb

Example Key2

* public: 034d6c013aa68cd2e0a7c247eef627586481c35eb00c7f2e1698d6a054059e0b52
* private: f66641a724879c43444a6ea8aab5da9fd58d5cbffdbf22bbc0ceefc2626cb0c9

You can then send request for a new address by sending post request on to the running server on the /rest/addresses url
with the Json content similar to this one:

    {
        "totalKeys":3,
        "requiredKeys":2,
        "keys":["02c8064852f9e5d30e10997cdc4da43238016eeefbc20b439a8eb136546992786c","034d6c013aa68cd2e0a7c247eef627586481c35eb00c7f2e1698d6a054059e0b52"]"]
    }
    
The request should succeed and you should get back response similar to this one:

    {
        "address":"2NDyZrJZamhofJpkPqCpx6Tcf6ECPzg1ZU1",
        "redeemScript":"522102c8064852f9e5d30e10997cdc4da43238016eeefbc20b439a8eb136546992786c21034d6c013aa68cd2e0a7c247eef627586481c35eb00c7f2e1698d6a054059e0b5221025dbf5f6db8ad000723cbc0f5ff68f9133d4bfe2752373dab29e4e5ce8f19272c53ae",
        "totalKeys":3,
        "requiredKeys":2,
        "balance":0,
        "keys":["02c8064852f9e5d30e10997cdc4da43238016eeefbc20b439a8eb136546992786c","034d6c013aa68cd2e0a7c247eef627586481c35eb00c7f2e1698d6a054059e0b52","025dbf5f6db8ad000723cbc0f5ff68f9133d4bfe2752373dab29e4e5ce8f19272c"],
        "links":[{"rel":"self","href":"http://localhost/rest/addresses/2NDyZrJZamhofJpkPqCpx6Tcf6ECPzg1ZU1"}]
    }

Here you can see the newly created address as well as the server public key. You should be now able to send get request to the link provided to get new status of the address. (Could be useful for getting balance?) 
You can now send some bitcoins to the address. And see that the Get request will update you with the new balance.

Creating transaction
--------------------
When you will have enough balance, you can now send request for creating new transaction for the address. You want to send
Post request to the URL /rest/transactions with the content similar to this one:

    {
        "targetAddress":"mkXBTS6T6JmJjKpto9QJmCDWxb5nxg3GAq",
        "amount":20000,
        "sourceAddress":"2NDyZrJZamhofJpkPqCpx6Tcf6ECPzg1ZU1"
    }
 
 This should give you response of the following form:
 
    {
        "targetAddress":"mkXBTS6T6JmJjKpto9QJmCDWxb5nxg3GAq",
        "amount":20000,
        "sourceAddress":"2NDyZrJZamhofJpkPqCpx6Tcf6ECPzg1ZU1",
        "rawTransaction":"01000000012330d70f9c8d8059911684f54a2a5751b3090386a0ce57144a3977c0704275d20100000000ffffffff01204e0000000000001976a91436e39da2c6420f1ae8d7c1d80c63e0004c863d4d88ac00000000",
        "links":[{"rel":"self","href":"http://localhost/rest/transactions/1"}]
    }
    
Signing the transaction
-----------------------
  
You now need to sign the transaction (we want to use 2/3 signing here. And send Post request to the URL that was given to you in the previous response of the following form:

    {
        "targetAddress":"mkXBTS6T6JmJjKpto9QJmCDWxb5nxg3GAq",
        "amount":20000,
        "sourceAddress":"2NDyZrJZamhofJpkPqCpx6Tcf6ECPzg1ZU1",
        "rawTransaction":"01000000012330d70f9c8d8059911684f54a2a5751b3090386a0ce57144a3977c0704275d201000000b30046304302206a707bad8edfb5f0cdbfeea3f7cd19dae847a5fc3966bdf1990c20867f431d53021f590e8bb6a3d06d0b6b2cdc3f93ce7ee6d5ff75805ad2fdb97125cb6bcc825b814c69522102c8064852f9e5d30e10997cdc4da43238016eeefbc20b439a8eb136546992786c21034d6c013aa68cd2e0a7c247eef627586481c35eb00c7f2e1698d6a054059e0b5221025dbf5f6db8ad000723cbc0f5ff68f9133d4bfe2752373dab29e4e5ce8f19272c53aeffffffff01204e0000000000001976a91436e39da2c6420f1ae8d7c1d80c63e0004c863d4d88ac00000000",
    }
 
And you will receive back the transaction signed by the server like this:

    {
        "targetAddress":"mkXBTS6T6JmJjKpto9QJmCDWxb5nxg3GAq",
        "amount":20000,"sourceAddress":"2NDyZrJZamhofJpkPqCpx6Tcf6ECPzg1ZU1",
        "rawTransaction":"01000000012330d70f9c8d8059911684f54a2a5751b3090386a0ce57144a3977c0704275d201000000fb0046304302206a707bad8edfb5f0cdbfeea3f7cd19dae847a5fc3966bdf1990c20867f431d53021f590e8bb6a3d06d0b6b2cdc3f93ce7ee6d5ff75805ad2fdb97125cb6bcc825b8147304402207c0ac5189b48eb31def32827151340654751aca5a95a375009982bf3f455ee8f02201dc2b14b0b830167e9a87f92888786b19890565838eb482ced4fd073dec684bd814c69522102c8064852f9e5d30e10997cdc4da43238016eeefbc20b439a8eb136546992786c21034d6c013aa68cd2e0a7c247eef627586481c35eb00c7f2e1698d6a054059e0b5221025dbf5f6db8ad000723cbc0f5ff68f9133d4bfe2752373dab29e4e5ce8f19272c53aeffffffff01204e0000000000001976a91436e39da2c6420f1ae8d7c1d80c63e0004c863d4d88ac00000000",
        "links":[{"rel":"self","href":"http://localhost/rest/transactions/1"}]
    }
    
That is basically what is supported so far.


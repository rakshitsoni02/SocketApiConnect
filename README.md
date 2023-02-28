# Socket JAVA SDK
The  **Socket**  SDK makes calling socket api easy.

----------
Usage
------

Just add the library as module in your app to use via socket in mobile apps.
https://www.viasocket.com/

    SocketApiConnect socketApiConnect = new SocketApiConnect("YOUR_FLOW_ID", "YOUR_TEAM_AUTHKEY");
                           HashMap<String, Argument> map = new HashMap<>();
                           map.put("TEST_KEY", new Argument("data", "test_value"));
                           socketApiConnect.call(map);




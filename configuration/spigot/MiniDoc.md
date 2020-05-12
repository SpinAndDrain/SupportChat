# SupportChat Configurations - Spigot

*Config Version: 6.2*

### Quick navigation

* [Time Strings](#time-strings)
* [Addons](#addons)
* [Rewards](#rewards)
* [Config](#config)
* [Messages](#messages)
* [MySQL](#mysql)
* [Reasons](#reasons)

### Time Strings

Time strings contains out of a value (a number, the amount) and a time format. (example: ```2h``` <- means two hours)

Timeformat / Contraction | Unit
------------------------ | ----
ms | millisecond(s)
s | second(s)
m | minute(s)
h | hour(s)
d | day(s)
w | week(s)
mo | month(s)
y | year(s)

## Addons

**File:** addons/addons.yml

**FAQ**

* ```enable: true``` If FAQ should be enabled set this to *true*, otherwise to *false*.
* ```
  message:
  - '&cF'
  - '&aA'
  - '&6Q'
  ```
  This message is shown when a player executes the **/faq** command. Each dash is a new line in the chat.
  
**Action Bar**

* ```enable: true``` If the action bar should be enabled set this to *true*, otherwise to *false*.
* ```message: '&b[count] &cSupporters online'``` The message which should be shown in the action bar.
* ```empty: '&cNo supporters online!'``` The message which should be shown in the action bar if no supporter is online.
* **fadeout**
  * ```enable: false``` If you set this to *true* the action bar will be shown continuous.
  * ```cooldown: 3s``` Defines how long the action bar should stay (only works with *fadeout.enable = false*).
* **events** Defines when and for whom the action bar should be shown.
  * ```on-join: ALL``` Triggered when a player joins the server.
  * ```on-move: NONE``` Triggered when a player moves.
  * ```on-supporter-login: ALL``` Triggered when a supporter logs in.
  * ```on-supporter-logout: ALL``` Triggered when a supporter logs out.
  * ```on-loglist-view: ACTOR``` Triggered when a player executes the **/loglist** command.
  * *for all events above this line are the following values valid:*

    Type | Affected
    ---- | --------
    ALL | Every online player on the server.
    ACTOR | The executing player of the respective event.
    NONE | No player.
  
  * ```send-each: 0ms``` Defines how often the action bar should be shown by the given interval (0 = disabled).

**Essentials AFK-Hook**

*Required Plugin: [EssentialsX](https://www.spigotmc.org/resources/essentialsx.9089/)*

* ```enable: false``` If the afk-hook should be enabled set this to *true*, otherwise to *false*.
* ```login: '&7~ You are logged in again'``` The message when a player gets logged in after being afk.
* ```logout: '&7~ You got logged out'``` The message when a player gets logged out while going afk.
* ```login-notification: '&b[player] &egot logged in automatically.'``` The message for other logged in supporters when the player gets logged in after being afk.
* ```logout-notification: '&b[player] &egot logged out automatically.'``` The message for other logged in supporters when a player gets logged out while going afk.

## Rewards

**File:** addons/rewards.yml

*Extra example [here](https://github.com/SpinAndDrain/SupportChat/blob/master/examples/rewards.yml).*

* ```enable: true``` If a supporter should be rewarded after finishing a conversation, set this to *true*.

Placeholder | Replacement
----------- | -----------
[player] | The player as *org.bukkit.entity.Player*.
[player-name] | The player's name as *String*.
[player-id] | The player's UUID as *String*.

## Config

**File:** config.yml

* ```join-login: DISABLED``` Defines the way a supporter should be logged in when joining the server.

  Type | Description
  ---- | -----------
  FULL | Each supporter gets automatically logged in.
  HIDDEN | Each supporter gets automatically logged in **hidden**.
  PERMISSION_RANGE | The supporter gets automatically logged in according his highest permission.
  DISABLED | No automatic login.

* **updater**
  * ```check-on-startup: true``` The plugin checks for newer versions on each startup (in the console) if this is set to *true*.
  * ```auto-download: true``` The plugin automatically downloads (if available) the newer version (works only with ```check-on-startup: true```).
* ```auto-notification: 2m``` All supporters receiving a message if support requests are still open by the given interval (0 = disabled).
* ```request-delay: 10m``` Defines the delay between each support request of a player (this should prevent spam, 0 = disabled).
* ```request-auto-delete-after: 1d``` Defines the interval after a support request should be automatically deleted if no supporter is handling it (0 = disabled).

## Messages

**File:** messages.yml

* ```language-file: en.lang``` Defines the language file from which all the messages should be read out.
* **Important:** The file must be in the *messages* folder. You can still edit each message by your own - **.lang** files can be easily edited by a normal text editor. If you need any help see [this little documentation](https://github.com/SpinAndDrain/LibsCollection/blob/master/libraries/LScript.md).

## MySQL

**File:** mysql.yml

* ```use: false``` *true* if you want to use MySQL.
* ```host: localhost``` Defines the host of your MySQL server.
* ```port: 3306``` Defines the port of your MySQL server.
* ```database: supportchat``` Defines the target database.
* ```user: root``` Defines the username for the connection.
* ```password: pw123``` Defines the password for the connection.
* ```useSSL: false``` *true* if you want to use SSL while connecting/connected.

## Reasons

**File:** reasons.yml

* ```mode: ENABLED``` Defines the support request mode.

  Mode | Description
  ---- | -----------
  ENABLED | A reason is required and must be chosen from given reasons.
  DISABLED | A reason is required but can be chosen by the player.
  ABSOLUTE_DISABLED | No reason required. The player can choose whether to give a reason.
  
* ```
  reasons:
  - Set
  - Your
  - Reasons
  - Here!
  ```
  Defines the given reasons. Each dash is a new reasons. (only works with ```mode: ENABLED```).

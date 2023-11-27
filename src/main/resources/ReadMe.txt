#### DCsCustomSpawnMod R4.0.1 for Minecraft 1.19.2 ####

# 導入環境 / Required Environment

・Minecraft1.19.2
・MincraftForge 1.19.2_43.2.0+

# 使用方法 / Installation

・MincraftForgeの導入後に作成されるmodsフォルダの中に、DLしたファイルを.jarファイルのまま置いてください。
　After installing MincraftForge, put the downloaded jar file into the generated mods folder.
・サーバーサイドのみの導入で動作します。(多分)
　Probably it works only on the server side.


# 配布DL先 / Published links
 Wiki：https://defeatedcrow.jp/modwiki/CustomSpawn


# 概要 / Summery
　Mob(LivingEntity)の自然スポーンを制限するmodです。
　This mod restricts the natural spawning of mobs (LivingEntity).

# 使い方 / Use

 導入するとコンフィグフォルダ内に"custom_spawn"フォルダと、コンフィグファイルの作成例が生成されます。
 制限したいmobごとに、設定ファイルを作成してcustom_spawnフォルダに追加してください。
 When installed, a "custom_spawn" folder and an example configuration file will be generated in the config folder.
 Create a config file for each mob you want to restrict, and add it to "custom_spawn" folder.

# 設定ファイルの見方 / About config file

EntityTypeName:
　対象のEntityTypeを指定します。　Specifies the target EntityType.
 * 指定した文字列を含むEntityが対象となります。（部分一致）
   Entities that contain the specified string are targeted. (Partial Match)
 * modidを含めることは出来ません。（":"などの制御文字は使用できません。）
   modid cannot be included. (Control characters such as ":" are not allowed.)

MaxSpawnAltitude:
 スポーンを許可する最高高度を指定します。
 Specifies the maximum altitude allowed to spawn.

MinSpawnAltitude:
  スポーンを許可する最低高度を指定します。
 Specifies the minimum altitude allowed to spawn.

DenySpawnInVillage:
 "true"の場合、村の内部でのスポーンを無効化します。
 If "true", disables spawning inside villages.

DenySpawnerSpawn:
 "true"の場合、スポナーブロックによるスポーンを無効化します。
 If "true", disables spawning from the spawner.

BiomeTagBlacklist:
 スポーン座標のバイオームが指定した文字列を含むBiomeTagを持つ場合、スポーンを無効化します。
 Disables spawning if the biome at the spawn coordinates has a BiomeTag containing the given string.

BiomeTagBlacklist:
 スポーン座標のBlockStateが指定した文字列を含むBlockTagを持つ場合、スポーンを無効化します。
 Disables spawning if the BlockState at the spawn coordinates has a BlockTag containing the given string.


 ### 更新履歴 / Change log

 ○v4.0.1
  Forge43.2.0に対応

 ○v4.0.1
  MC1.19.2に対応

 ○B3.0.0(beta)
 　MC1.15.2に対応

 ○v2.1.1
 　スポーン条件を再修正

 ○v2.1.0
 　ブロック、バイオームのブラックリストを追加

 ○v2.0.1
 　うまく動作しないためEvent発生条件を修正
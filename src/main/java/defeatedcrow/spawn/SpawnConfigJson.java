package defeatedcrow.spawn;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import net.minecraft.world.entity.EntityType;

public class SpawnConfigJson {

	public static final SpawnConfigJson INSTANCE = new SpawnConfigJson();
	public static List<SpawnData> defaultData = Lists.newArrayList();
	public static List<SpawnData> configData = Lists.newArrayList();

	public static void initFile() {

		// 生成は最初のみ
		if (CustomSpawnCore.configDir == null)
			return;

		initRegistry();

		// configフォルダに生成する
		for (SpawnData table : INSTANCE.defaultData) {
			File f = new File(CustomSpawnCore.configDir, table.getFileName() + ".json");

			// すでにファイルが有る場合は何もしない。
			if (!f.exists()) {

				// ファイルを生成する。
				if (!f.getParentFile().exists()) {
					f.getParentFile().mkdirs();
				}

				try {
					f.createNewFile();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				// JSONファイルの生成。
				try {
					if (f.canWrite()) {
						// Streamは開けたら閉めるを徹底しよう。ご安全に!
						FileOutputStream fos = new FileOutputStream(f.getPath());
						OutputStreamWriter osw = new OutputStreamWriter(fos);
						JsonWriter jsw = new JsonWriter(osw);

						// ここでインデントのスペースの数を調整でき、ファイルの内容が適宜改行されるようになる。
						jsw.setIndent("  ");
						Gson gson = new GsonBuilder().serializeNulls().disableHtmlEscaping().create();
						gson.toJson(table, SpawnData.class, jsw);

						osw.close();
						fos.close();
						jsw.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void loadFiles() {

		if (CustomSpawnCore.configDir == null || !CustomSpawnCore.configDir.exists())
			return;

		File[] files = CustomSpawnCore.configDir.listFiles(new JsonFileFilter());

		if (files != null) {
			try {
				for (File file : files) {
					if (file.canRead()) {
						FileInputStream fis = new FileInputStream(file.getPath());
						InputStreamReader isr = new InputStreamReader(fis);
						JsonReader jsr = new JsonReader(isr);
						Gson gson = new Gson();
						SpawnData get = gson.fromJson(jsr, SpawnData.class);

						isr.close();
						fis.close();
						jsr.close();

						if (get != null && !get.getFileName().equalsIgnoreCase("empty")) {
							INSTANCE.configData.add(get);
							CustomSpawnCore.LOGGER.info("Register custom spawn data: " + get.getFileName());
						}
					}
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void initRegistry() {
		INSTANCE.defaultData.add(new SpawnData(EntityType.ZOMBIE, 50, -64, true, false, new String[] { "Set any biome tags. ex. SANDY, FOREST, HOT..." }, new String[] { "Set any block tags" }));
		INSTANCE.defaultData.add(new SpawnData(EntityType.SKELETON, 50, -64, true, false, new String[] { "Set any biome tags" }, new String[] { "Set any block tags" }));
		INSTANCE.defaultData.add(new SpawnData(EntityType.CREEPER, 50, -64, true, false, new String[] { "Set any biome tags" }, new String[] { "Set any block tags" }));
		INSTANCE.defaultData.add(new SpawnData(EntityType.SPIDER, 50, -64, true, false, new String[] { "Set any biome tags" }, new String[] { "Set any block tags" }));
		INSTANCE.defaultData.add(new SpawnData(EntityType.SLIME, 50, -64, true, false, new String[] { "Set any biome tags" }, new String[] { "Set any block tags" }));
		INSTANCE.defaultData.add(new SpawnData(EntityType.ENDERMAN, 50, -64, true, false, new String[] { "Set any biome tags" }, new String[] { "Set any block tags" }));
		INSTANCE.defaultData.add(new SpawnData(EntityType.WITCH, 50, -64, true, false, new String[] { "Set any biome tags" }, new String[] { "Set any block tags" }));
	}

}

package defeatedcrow.spawn.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.registries.ForgeRegistries;

public class SpawnBiomeList {

	private static final Logger LOGGER = LogManager.getLogger("dcs_spawn");
	public static final Map<EntityType<?>, List<BiomeType>> biomeList = new HashMap<EntityType<?>, List<BiomeType>>();

	public static final SpawnBiomeList INSTANCE = new SpawnBiomeList();

	/* json */
	private static Map<String, Object> jsonMap = new HashMap<String, Object>();
	public static Map<String, List<String>> regMap = new HashMap<String, List<String>>();

	private static File dir = null;

	public void addMobBlackList(String entity, List<String> biomes) {
		if (entity != null && biomes != null) {
			regMap.put(entity, biomes);
		}
	}

	public void startMap() {
		if (!jsonMap.isEmpty()) {
			for (Entry<String, Object> ent : jsonMap.entrySet()) {
				if (ent != null) {
					String name = ent.getKey();
					Object value = ent.getValue();
					List<String> data = Lists.newArrayList();
					if (value instanceof List) {
						data.addAll((List<String>) value);
					}
					regMap.put(name, data);
				}
			}
		}

		if (!regMap.isEmpty()) {
			for (Entry<String, List<String>> ent : regMap.entrySet()) {
				if (ent != null && DCSpawnConfig.isValidKey(ent.getKey())) {
					String name = ent.getKey().toLowerCase(Locale.ROOT);
					List<String> value = ent.getValue();
					EntityType<?> entity = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(name));
					if (value != null && !value.isEmpty() && entity != null) {
						List<BiomeType> data = Lists.newArrayList();
						String l = "";
						for (String s : value) {
							BiomeType b = getType(s);
							if (b != null) {
								data.add(b);
								l += b.toString() + ", ";
							}
						}
						if (entity != null && !data.isEmpty()) {
							biomeList.put(entity, data);
							LOGGER.info("Add spawn blacklist: " + name + ": " + l);
						}
					}
				}
			}
		}
	}

	public void pre() {
		jsonMap.clear();
		if (dir != null) {
			try {
				if (!dir.exists() && !dir.createNewFile()) {
					return;
				}

				if (dir.canRead()) {
					FileInputStream fis = new FileInputStream(dir.getPath());
					InputStreamReader isr = new InputStreamReader(fis);
					JsonReader jsr = new JsonReader(isr);
					Gson gson = new Gson();
					Map get = gson.fromJson(jsr, Map.class);

					isr.close();
					fis.close();
					jsr.close();

					if (get != null && !get.isEmpty()) {
						jsonMap.putAll(get);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		startMap();
	}

	// 生成は初回のみ
	public void post() {

		if (dir != null) {
			try {
				if (!dir.exists() && !dir.createNewFile()) {
					return;
				} else if (!jsonMap.isEmpty()) {
					return;
				}

				if (dir.canWrite()) {
					FileOutputStream fos = new FileOutputStream(dir.getPath());
					OutputStreamWriter osw = new OutputStreamWriter(fos);
					JsonWriter jsw = new JsonWriter(osw);
					jsw.setIndent(" ");
					Gson gson = new Gson();
					gson.toJson(regMap, Map.class, jsw);

					osw.close();
					fos.close();
					jsw.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void setDir(File file) {
		dir = new File(file, "customspawn_biomelist.json");
		if (dir.getParentFile() != null) {
			dir.getParentFile().mkdirs();
		}
	}

	public BiomeType getType(String name) {
		if (name.equalsIgnoreCase("ALL")) {
			return new AllBiome();
		}
		Biome b = ForgeRegistries.BIOMES.getValue(new ResourceLocation(name));
		BiomeDictionary.Type t = BiomeDictionary.Type.getType(name);
		if (b != null || t != null) {
			return new BiomeType(b, t);
		} else {
			return null;
		}
	}

	public boolean canSpawn(Entity entity, Biome biome) {
		if (entity == null || biome == null)
			return true;
		for (Entry<EntityType<?>, List<BiomeType>> ent : biomeList.entrySet()) {
			if (entity.getType() == ent.getKey()) {
				for (BiomeType t : ent.getValue()) {
					if (t.match(biome)) {
						return false;
					}
				}
			}
		}
		return true;
	}

	public class BiomeType {
		public final Biome biome;
		public final BiomeDictionary.Type type;

		public BiomeType(Biome b, BiomeDictionary.Type t) {
			biome = b;
			type = t;
		}

		public boolean match(Biome target) {
			if (biome != null && target == biome) {
				return true;
			}
			if (type != null && BiomeDictionary.hasType(target, type)) {
				return true;
			}
			return false;
		}

		@Override
		public String toString() {
			if (biome != null)
				return biome.getRegistryName().toString();
			if (type != null)
				return type.getName();
			return "empty";

		}
	}

	public class AllBiome extends BiomeType {

		public AllBiome() {
			super(Biomes.PLAINS, null);
		}

		@Override
		public boolean match(Biome target) {
			return true;
		}

		@Override
		public String toString() {
			return "ALL";

		}
	}

}

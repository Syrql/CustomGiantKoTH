package fr.syrql.giantkoth.provider;

import fr.syrql.giantkoth.CustomGiantKoTH;
import fr.syrql.giantkoth.data.KothPointsData;
import fr.syrql.giantkoth.io.FileUtils;
import fr.syrql.giantkoth.io.IOUtil;
import fr.syrql.giantkoth.io.provide.IProvider;
import fr.syrql.giantkoth.io.readable.IReadable;
import fr.syrql.giantkoth.io.writable.IWritable;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class KothPointsProvider implements IProvider<String, KothPointsData>, IWritable, IReadable {

    private final CustomGiantKoTH customGiantKoTH;
    private final File save;
    private Map<String, KothPointsData> arenaMap;

    public KothPointsProvider(CustomGiantKoTH customGiantKoTH) {
        this.customGiantKoTH = customGiantKoTH;
        this.arenaMap = new HashMap<>();
        this.save = new File(this.customGiantKoTH.getDataFolder(), "/kothpoints/");
    }

    @Override
    public void provide(String key, KothPointsData value) {
        this.arenaMap.put(key, value);
        this.write();
    }

    @Override
    public void remove(String key) {

        KothPointsData user = this.arenaMap.get(key);

        if (user == null) return;

        this.arenaMap.remove(key);

        final File file = new File(save, user.getKothName() + ".json");
        if (file.delete()) System.out.println("Giant KoTH - Deleted file " + file.getName());

        this.write();
    }

    @Override
    public KothPointsData get(String key) {
        return this.getKothPoints().stream().filter(kothData -> kothData.getKothName().equalsIgnoreCase(key)).findFirst().orElse(null);
    }

    public boolean exist(KothPointsData kothData) {
        return kothData.getArea() != null && kothData.getCaptureZone() != null;
    }

    @Override
    public void read(boolean reload) {

        File[] files = save.listFiles();

        if (files == null) {
            this.arenaMap = new HashMap<>();
            return;
        }
        IOUtil ioUtil = this.customGiantKoTH.getIoUtil();

        for (File file : files) {
            if (file.isFile()) {
                final String json = FileUtils.loadContent(file);
                KothPointsData user = ioUtil.deserializeCitadel(json);
                this.arenaMap.put(user.getKothName(), user);
            }
        }
    }

    @Override
    public void write() {

        if (this.arenaMap == null) return;

        final IOUtil ioUtil = this.customGiantKoTH.getIoUtil();

        for (KothPointsData user : this.getKothPoints()) {
            final File file = new File(save, user.getKothName() + ".json");
            final String json = ioUtil.serializeGiantKoTH(user);
            FileUtils.save(file, json);
        }
    }

    public Collection<KothPointsData> getKothPoints() {
        return this.arenaMap.values();
    }
}

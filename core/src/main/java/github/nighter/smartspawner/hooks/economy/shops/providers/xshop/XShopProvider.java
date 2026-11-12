package github.nighter.smartspawner.hooks.economy.shops.providers.xshop;

import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.hooks.economy.shops.providers.ShopProvider;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;

/**
 * Sell prices from xShop. xShop is not published to a Maven repository, so its API is reached by
 * reflection instead of a compile-time dependency:
 * {@code XShop#getSellManager()} then {@code SellManager#getSellEntry(Material)} then
 * {@code SellEntry#getPrice()}. The lookup reads xShop's current price snapshot on every call, so an
 * {@code /xshop reload} is picked up without reloading SmartSpawner.
 */
@RequiredArgsConstructor
public class XShopProvider implements ShopProvider {
    static final String PLUGIN_NAME = "xShop";

    private final SmartSpawner plugin;
    private Plugin xShop;
    private Method getSellManager;
    private Method getSellEntry;
    private Method getPrice;

    @Override
    public String getPluginName() {
        return PLUGIN_NAME;
    }

    @Override
    public boolean isAvailable() {
        try {
            Plugin found = Bukkit.getPluginManager().getPlugin(PLUGIN_NAME);
            if (found == null || !found.isEnabled()) {
                return false;
            }
            Method sellManagerMethod = found.getClass().getMethod("getSellManager");
            Class<?> sellManagerClass = sellManagerMethod.getReturnType();
            Method entryMethod = sellManagerClass.getMethod("getSellEntry", Material.class);
            Method priceMethod = entryMethod.getReturnType().getMethod("getPrice");

            this.xShop = found;
            this.getSellManager = sellManagerMethod;
            this.getSellEntry = entryMethod;
            this.getPrice = priceMethod;
            return true;
        } catch (NoSuchMethodException e) {
            plugin.getLogger().warning("xShop is installed but its sell API was not found ("
                    + e.getMessage() + "). Update xShop to a version with SellManager#getSellEntry.");
        } catch (Exception | LinkageError e) {
            plugin.getLogger().warning("Error initializing xShop integration: " + e.getMessage());
        }
        return false;
    }

    @Override
    public double getSellPrice(Material material) {
        if (xShop == null || material == null || !xShop.isEnabled()) {
            return 0.0;
        }
        try {
            Object sellManager = getSellManager.invoke(xShop);
            if (sellManager == null) {
                return 0.0;
            }
            Object entry = getSellEntry.invoke(sellManager, material);
            if (entry == null) {
                return 0.0;
            }
            double price = (double) getPrice.invoke(entry);
            return price > 0 ? price : 0.0;
        } catch (Exception e) {
            return 0.0;
        }
    }
}

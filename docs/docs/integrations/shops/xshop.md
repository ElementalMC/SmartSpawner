---
title: xShop
---

# ![](/icons/integrations/xshop.png){.plugin-icon} xShop

SmartSpawner reads sell prices from xShop's sell files. Auto-detection picks xShop when no other supported shop plugin is installed, or you can select it explicitly.

## Identifier

- `xShop`

## Configuration

In `sell_integration.yml`:

```yaml
enabled: true
currency: VAULT
price_source_mode: SHOP_PRIORITY
shop_integration:
  enabled: true
  preferred_plugin: xShop
```

## How prices are read

- Each item is priced by its plain material entry in xShop's sell files, such as `STONE`. Potion and enchanted book entries are not used, because spawner loot is priced per material.
- The price is xShop's base price. xShop's own sell multipliers do not apply to spawner sales.
- Prices are read live, so an `/xshop reload` takes effect without reloading SmartSpawner.
- Money is paid in SmartSpawner's `currency`, not in the currency of the xShop sell category.

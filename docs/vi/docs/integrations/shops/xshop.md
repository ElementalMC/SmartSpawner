---
title: xShop
---

# ![](/icons/integrations/xshop.png){.plugin-icon} xShop

SmartSpawner đọc giá bán từ các tệp bán của xShop. Chế độ tự động phát hiện chọn xShop khi không có plugin cửa hàng nào khác được hỗ trợ, hoặc bạn có thể chọn thủ công.

## Định danh

- `xShop`

## Cấu hình

Trong `sell_integration.yml`:

```yaml
enabled: true
currency: VAULT
price_source_mode: SHOP_PRIORITY
shop_integration:
  enabled: true
  preferred_plugin: xShop
```

## Cách đọc giá

- Mỗi vật phẩm được định giá theo mục vật liệu thường trong tệp bán của xShop, ví dụ `STONE`. Mục thuốc và sách phù phép không được dùng.
- Giá là giá gốc của xShop. Hệ số nhân bán của xShop không áp dụng cho việc bán từ spawner.
- Giá được đọc trực tiếp, nên `/xshop reload` có hiệu lực mà không cần tải lại SmartSpawner.
- Tiền được trả bằng `currency` của SmartSpawner, không phải tiền tệ của danh mục bán trong xShop.

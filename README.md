# 🛍️ ShopEasy — Visitor Pattern Shopping Cart

<p align="center">
  <img width="986" height="673" alt="image" src="https://github.com/user-attachments/assets/2552e461-c967-4f1a-a244-f561bd730d69" />
</p>

**Repository:** `https://github.com/Tharindu714/Shopping-Cart-Visitor-Application.git`

> A colourful Java Swing demo that illustrates the **Visitor** design pattern for a shopping cart. Different item types (Book, Electronics, Clothing) accept visitors that perform operations such as printing details, totaling prices, and applying discounts — without modifying the item classes themselves.

---

## ✨ Highlights

* 🎯 **Visitor pattern**: separate operations (Visitors) from item structure. Add new operations (tax, loyalty points, export) without changing `Item` classes.
* 🧾 **Operations implemented as Visitors**: `PrintDetailsVisitor`, `TotalPriceVisitor`, `DiscountVisitor` (examples included).
* 🛒 **E-commerce styled UI**: add items to cart, run operations, and view results in a friendly GUI.
* 🛡️ **Anti-pattern rules applied**: avoid mixing business operations into item classes; visitors are pure computations and do not mutate items.

---

## 🚀 Features

* Add `Book`, `Electronics`, or `Clothing` items to the cart using the product form.
* Run visitors to:

  * Print human-readable cart details.
  * Calculate total price.
  * Calculate discounted total using sample rules (books 5% off, clothing 10% off, electronics discount thresholds).
* Seeded sample items to try the demo quickly.

---

## 🛠️ Build & Run

Requires **Java 8+**.

```bash
# from repo root
javac ShoppingCart_Visitor_GUI.java
java ShoppingCart_Visitor_GUI
```

The application launches a ShopEasy window where you can add items and execute visitor operations. The console prints logs for debugging.

---

## 🧭 Design Overview

**Core components**

* `Item` (interface): common contract for items. Each concrete item implements `accept(Visitor)`.
* `Book`, `Electronics`, `Clothing` — concrete item types holding intrinsic state.
* `Visitor` (interface): operations that can be performed on items (visit methods for each concrete item).
* `PrintDetailsVisitor`, `TotalPriceVisitor`, `DiscountVisitor` — example visitors demonstrating different behaviors.
* `ShoppingCart` — client that holds items and applies visitors.
* `ShopFrame` / `ShopHeader` — Swing UI and attractive header.

**Why Visitor?**

* The Visitor pattern lets you add new operations over a fixed set of item classes without modifying those classes — ideal for evolving functionality in a shopping platform.

---

## ✅ Anti-patterns avoided (rules applied)

* ❌ **No mixing operations into item classes** — items remain simple data holders.
* ✅ **Visitors compute results without side-effects** (by default); mutation would be explicit and documented.
* ✅ **No long switch/if chains** for operation selection — polymorphism via Visitor dispatches to the correct method.
* ⚠️ **Extensibility caveat**: adding a new concrete `Item` type requires updating all existing Visitor implementations (classic Visitor tradeoff). Consider using double-dispatch alternatives if item types change frequently.

---

## 📐 UML (PlantUML)

Paste into [https://www.plantuml.com/plantuml](https://www.plantuml.com/plantuml) to render diagrams.

### Class Diagram

<p align="center">
  <img width="895" height="522" alt="UML-Light" src="https://github.com/user-attachments/assets/e4c88532-c44f-4150-aad2-40651920c05a" />
</p>

### Sequence Diagram (Calculate Discounted Total)

<p align="center">
  <img width="919" height="451" alt="Seq-Light" src="https://github.com/user-attachments/assets/d60de8c2-aa26-48eb-b197-4f0ffbe51541" />
</p>

---

## 📸 Scenario Screenshot

  ![Scenario 11](https://github.com/user-attachments/assets/7bb85e08-f17e-4713-9a61-bb94ac5b6b86)

---

## 🧪 Quick demo

1. Launch the app.
2. Add sample items or use seeded items.
3. Click **Print Details** to see item lines.
4. Click **Calculate Total** for the sum.
5. Click **Calculate Discounted Total** to see discounts applied.

---

## 🔧 Extensions & Production Notes

* Add more visitors: tax calculation, loyalty points, export to CSV/JSON, promotional rules engine.
* For large catalogs, decouple UI from business logic and add unit tests for visitors.
* If item types change frequently, consider the Strategy pattern or a hybrid design to reduce Visitor maintenance.

---

## 📮 Contribution

Fork, add features (new visitors, export, persistence), and submit PRs. Include tests demonstrating visitor results.

---

## 📝 License

MIT — reuse and adapt freely.

---

Made with care — Tharindu's ShopEasy Visitor Pattern demo. Happy shopping! 🎉

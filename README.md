# snack-scan-be

┌─────────────────────────────────────────────────────────────────────────────────┐
│                              SNACK SCAN 시스템                                  │
└─────────────────────────────────────────────────────────────────────────────────┘

┌─────────────┐    1:N    ┌─────────────────┐    N:1    ┌─────────────┐
│    Store    │◄─────────►│  StoreProduct   │◄─────────►│   Product   │
│             │           │                 │           │             │
│ - id        │           │ - id            │           │ - id        │
│ - name      │           │ - minStock      │           │ - name      │
│ - address   │           │ - currentStock  │           │ - barcode   │
│ - phone     │           │ - storePrice    │           │ - category  │
└─────────────┘           └─────────────────┘           │ - price     │
       │                                                └─────────────┘
       │ 1:N                                                      │
       │                                                          │ 1:N
       ▼                                                          ▼
┌─────────────┐                                           ┌─────────────┐
│    Sale     │                                           │    Sale     │
│             │                                           │             │
│ - id        │                                           │ - id        │
│ - saleDate  │                                           │ - saleDate  │
│ - quantity  │                                           │ - quantity  │
│ - totalPrice│                                           │ - totalPrice│
└─────────────┘                                           └─────────────┘
       │                                                          │
       │ N:1                                                      │ N:1
       ▼                                                          ▼
┌─────────────┐                                           ┌─────────────┐
│    Store    │                                           │   Product   │
└─────────────┘                                           └─────────────┘

┌─────────────┐    1:N    ┌─────────────────┐    N:1     ┌─────────────┐
│    Store    │◄─────────►│ RestockOrderItem │◄─────────►│   Product   │
│             │           │                 │            │             │
│ - id        │           │ - id            │            │ - id        │
│ - name      │           │ - quantity      │            │ - name      │
│ - address   │           │ - unitPrice     │            │ - barcode   │
│ - phone     │           │                 │            │ - category  │
└─────────────┘           └─────────────────┘            │ - price     │
       │                          │                      └─────────────┘
       │ 1:N                      │ N:1
       ▼                          ▼
┌─────────────┐           ┌─────────────┐
│RestockOrder │◄─────────►│RestockOrder │
│             │           │             │
│ - id        │           │ - id        │
│ - orderDate │           │ - orderDate │
│ - status    │           │ - status    │
│ - totalAmount│          │ - totalAmount│
└─────────────┘           └─────────────┘
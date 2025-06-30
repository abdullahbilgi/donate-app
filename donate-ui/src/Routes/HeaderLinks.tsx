import { Permission } from "../permissions/permissions";

export const ROUTES: {
  label: string;
  path: string;
  permission?: Permission;
}[] = [
  {
    label: "Ürün Ekle",
    path: "/addProduct",
    permission: "create:product",
  },
  {
    label: "Ürünler",
    path: "/products",
  },
  {
    label: "Marketlerim",
    path: "/markets",
    permission: "view:markets",
  },
  {
    label: "Organizasyonlar",
    path: "/organization",
    permission: "view:organizations",
  },
  {
    label: "Siparislerim",
    path: "/orders",
    permission: "view:my-orders",
  },
  {
    label: "Basvurular",
    path: "/appliesOrganization",
    permission: "view:applies",
  },
];

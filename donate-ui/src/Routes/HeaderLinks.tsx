import { Permission } from "../permissions/permissions";

export const ROUTES: {
  label: string;
  path: string;
  permission?: Permission;
}[] = [
  {
    label: "Add Product",
    path: "/addProduct",
    permission: "create:product",
  },
  {
    label: "Products",
    path: "/products",
  },
  {
    label: "My Markets",
    path: "/markets",
    permission: "view:markets",
  },
  {
    label: "Organizations",
    path: "/organization",
    permission: "view:organizations",
  },
  {
    label: "My Orders",
    path: "/orders",
    permission: "view:my-orders",
  },
  {
    label: "Applies",
    path: "/appliesOrganization",
    permission: "view:applies",
  },
];

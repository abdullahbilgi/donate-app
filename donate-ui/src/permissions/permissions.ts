export const rolePermissions = {
  ADMIN: [
    "view:donations",
    "view:donateProducts",
    "edit:donations",
    "delete:donations",
    "view:users",
    "create:product",
    "view:organizations",
    "view:applies",
    "view:markets",
  ],
  BENEFACTOR: ["view:donateProducts", "view:my-orders"],
  USER: [
    "create:product",
    "create:donations",
    "view:donateProducts",
    "view:products",
    "view:my-orders",
    "view:markets",
  ],
} as const;

export type Role = keyof typeof rolePermissions; //admin or user

type RolePermissionMap = typeof rolePermissions;
export type Permission = RolePermissionMap[Role][number]; //view delete vs

export const rolePermissions = {
  ADMIN: [
    "view:donations",
    "view:donateProducts",
    "edit:donations",
    "delete:donations",
    "view:users",
    "create:product",
    "edit:product",
    "view:organizations",
    "view:applies",
    "view:markets",
    "create:organization",
    "apply:organization",
  ],
  BENEFACTOR: ["view:donateProducts", "view:my-orders"],
  USER: [
    "create:product",
    "view:donateProducts",
    "view:products",
    "view:my-orders",
    "view:markets",
    "apply:organization",
    "create:organization",
  ],
} as const;

export type Role = keyof typeof rolePermissions; //admin or user

type RolePermissionMap = typeof rolePermissions;
export type Permission = RolePermissionMap[Role][number]; //view delete vs

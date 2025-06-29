export const rolePermissions = {
  ADMIN: ["view:donations", "edit:donations", "delete:donations", "view:users"],
  BENEFACTOR: ["view:getAllDonations"],
  USER: [
    "create:product",
    "create:donations",
    "view:products",
    "view:my-orders",
  ],
} as const;

export type Role = keyof typeof rolePermissions; //admin or user

type RolePermissionMap = typeof rolePermissions;
export type Permission = RolePermissionMap[Role][number]; //view delete vs

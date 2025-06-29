import { Permission, Role, rolePermissions } from "../permissions/permissions";

export const hasPermission = (
  role: Role | null,
  permission: Permission
): boolean => {
  if (!role) return false;

  const permissions = rolePermissions[role] as readonly Permission[];
  return permissions.includes(permission); //verilen role yetkisi var mi yok mu
};

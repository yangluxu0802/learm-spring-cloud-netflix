package cn.luxu.bootstrap.service;

import cn.luxu.dto.Page;
import cn.luxu.model.SysPermission;
import cn.luxu.model.SysRole;

import java.util.Map;
import java.util.Set;

public interface SysRoleService {

	void save(SysRole sysRole);

	void update(SysRole sysRole);

	void deleteRole(Long id);

	void setPermissionToRole(Long id, Set<Long> permissionIds);

	SysRole findById(Long id);

	Page<SysRole> findRoles(Map<String, Object> params);

	Set<SysPermission> findPermissionsByRoleId(Long roleId);
}

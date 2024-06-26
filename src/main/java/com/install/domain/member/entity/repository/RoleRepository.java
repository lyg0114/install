package com.install.domain.member.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.install.domain.member.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

	@Query(
		"select r from Role r "
			+ "join fetch r.memberRoles mr "
			+ "where mr.member.id = :memberId"
	)
	List<Role> findRolesByMemberId(Long memberId);
}

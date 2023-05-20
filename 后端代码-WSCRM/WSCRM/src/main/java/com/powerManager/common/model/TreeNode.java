package com.powerManager.common.model;

import lombok.Data;

import java.util.List;

/**
 * 树节点
 * 继承此类然后使用ListUtil.treeify()进行list转树操作
 *
 * @author 编码时长两年半
 */
@Data
public class TreeNode<T extends TreeNode<T>> {
    private Long id;

    private Long parentId;

    private List<T> childList;
}

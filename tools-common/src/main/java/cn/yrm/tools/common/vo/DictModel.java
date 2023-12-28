package cn.yrm.tools.common.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DictModel implements Serializable{

	public DictModel() {
	}
	
	public DictModel(String value, String label) {
		this.value = value;
		this.label = label;
	}
	
	/**
	 * 字典value
	 */
	private String value;
	/**
	 * 字典文本
	 */
	private String label;
	/**
	 * 上级的id(Tree使用)
	 */
	private String pid;
	/**
	 * 子节点(Tree使用)
	 */
	private List<DictModel> children;

	/**
	 * text的别称
	 * @return
	 */
	public String getTitle() {
		return this.label;
	}

}

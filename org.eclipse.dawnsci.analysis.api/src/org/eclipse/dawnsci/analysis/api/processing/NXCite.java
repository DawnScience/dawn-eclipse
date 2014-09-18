/*-
 * Copyright 2014 Diamond Light Source Ltd.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.eclipse.dawnsci.analysis.api.processing;

public class NXCite {

	private String bibtex;
	private String description;
	private String doi;
	private String endnote;
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bibtex == null) ? 0 : bibtex.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((doi == null) ? 0 : doi.hashCode());
		result = prime * result + ((endnote == null) ? 0 : endnote.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NXCite other = (NXCite) obj;
		if (bibtex == null) {
			if (other.bibtex != null)
				return false;
		} else if (!bibtex.equals(other.bibtex))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (doi == null) {
			if (other.doi != null)
				return false;
		} else if (!doi.equals(other.doi))
			return false;
		if (endnote == null) {
			if (other.endnote != null)
				return false;
		} else if (!endnote.equals(other.endnote))
			return false;
		return true;
	}
	public String getBibtex() {
		return bibtex;
	}
	public void setBibtex(String bibtex) {
		this.bibtex = bibtex;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDoi() {
		return doi;
	}
	public void setDoi(String doi) {
		this.doi = doi;
	}
	public String getEndnote() {
		return endnote;
	}
	public void setEndnote(String endnote) {
		this.endnote = endnote;
	}
}

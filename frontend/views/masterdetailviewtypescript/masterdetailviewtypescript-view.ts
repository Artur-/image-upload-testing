import EntityModel from '../../generated/com/example/application/data/entity/EntityModel';
import * as EntityEndpoint from '../../generated/EntityEndpoint';
import Entity from '../../generated/com/example/application/data/entity/Entity';
import { customElement, html, LitElement, property, query, unsafeCSS } from 'lit-element';

import { showNotification } from '@vaadin/flow-frontend/a-notification';
import '@vaadin/vaadin-button/vaadin-button';
import '@vaadin/vaadin-form-layout/vaadin-form-layout';
import '@vaadin/vaadin-grid';
import { GridDataProviderCallback, GridDataProviderParams } from '@vaadin/vaadin-grid/vaadin-grid';
import '@vaadin/vaadin-grid/vaadin-grid-sort-column';
import '@vaadin/vaadin-ordered-layout/vaadin-horizontal-layout';
import '@vaadin/vaadin-split-layout/vaadin-split-layout';
import '@vaadin/vaadin-text-field/vaadin-text-field';
import '@vaadin/vaadin-upload';
import '@vaadin/vaadin-custom-field';
import '@vaadin/vaadin-date-picker';
import { EndpointError } from '@vaadin/flow-frontend/Connect';

import { Binder, field } from '@vaadin/form';
import { CSSModule } from '@vaadin/flow-frontend/css-utils';

import styles from './masterdetailviewtypescript-view.css';

@customElement('masterdetailviewtypescript-view')
export class MasterdetailviewtypescriptView extends LitElement {
  static get styles() {
    return [CSSModule('lumo-typography'), unsafeCSS(styles)];
  }

  @query('#grid')
  private grid: any;

  @property({ type: Number })
  private gridSize = 0;

  private gridDataProvider = this.getGridData.bind(this);

  private binder = new Binder(this, EntityModel);

  render() {
    return html`
      <vaadin-split-layout class="full-size">
        <div class="grid-wrapper">
          <vaadin-grid
            id="grid"
            class="full-size"
            theme="no-border"
            .size="${this.gridSize}"
            .dataProvider="${this.gridDataProvider}"
            @active-item-changed=${this.itemSelected}
          >
            /* generatorCall: generateGridColumns(EntityConfiguration) */
          </vaadin-grid>
        </div>
        <div id="editor-layout">
          <div id="editor">
            <vaadin-form-layout
              >/* generatorCall: generateFormFields(EntityConfiguration, this.binder, this.generatingCode, false) */</vaadin-form-layout
            >
          </div>
          <vaadin-horizontal-layout id="button-layout" theme="spacing">
            <vaadin-button theme="primary" @click="${this.save}">Save</vaadin-button>
            <vaadin-button theme="tertiary" @click="${this.cancel}">Cancel</vaadin-button>
          </vaadin-horizontal-layout>
        </div>
      </vaadin-split-layout>
    `;
  }

  private async getGridDataSize() {
    return EntityEndpoint.count();
  }

  private async getGridData(params: GridDataProviderParams, callback: GridDataProviderCallback) {
    const index = params.page * params.pageSize;
    const data = await EntityEndpoint.list(index, params.pageSize, params.sortOrders as any);
    callback(data);
  }

  // Wait until all elements in the template are ready to set their properties
  async firstUpdated(changedProperties: any) {
    super.firstUpdated(changedProperties);

    this.gridSize = await this.getGridDataSize();
  }

  private async itemSelected(event: CustomEvent) {
    const item: Entity = event.detail.value as Entity;
    this.grid.selectedItems = item ? [item] : [];

    if (item) {
      const entityFromBackend = await EntityEndpoint.get(item.id);
      entityFromBackend ? this.binder.read(entityFromBackend) : this.refreshGrid();
    } else {
      this.clearForm();
    }
  }

  private async save() {
    try {
      await this.binder.submitTo(EntityEndpoint.update);

      if (!this.binder.value.id) {
        // We added a new item
        debugger;
        this.gridSize++;
      }
      this.clearForm();
      this.refreshGrid();
      showNotification('Entity details stored.', { position: 'bottom-start' });
    } catch (error) {
      if (error instanceof EndpointError) {
        showNotification('Server error. ' + error.message, { position: 'bottom-start' });
      } else {
        throw error;
      }
    }
  }

  private cancel() {
    this.grid.activeItem = undefined;
  }

  private clearForm() {
    this.binder.clear();
  }

  private refreshGrid() {
    this.grid.selectedItems = [];
    this.grid.clearCache();
  }
}
